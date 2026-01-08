# Phase 3 Implementation Specifications: Core New Services

## Overview

**Timeline:** Weeks 12-18
**Focus:** Critical New Microservices
**Deliverables:** 9 new services operational, 5 care pathways, claims orchestration with Temporal

---

## 📋 Objectives

### Primary Goals:
1. Implement 9 new microservices to extend platform capabilities
2. Build 5 disease-specific care pathways (MVP for Phase 4)
3. Deploy Temporal workflows for insurance claims
4. Create provider directory and search
5. Implement patient engagement tracking
6. Build cost transparency and pricing services
7. Deploy document AI for medical record processing

### Success Criteria:
- ✅ All 9 services deployed and registered with Eureka
- ✅ 5 care pathways operational (Diabetes, Hypertension, Heart Failure, Asthma, Depression)
- ✅ Claims processing workflow running in Temporal
- ✅ Provider search API operational with <200ms latency
- ✅ Document AI extracting data from PDFs with >90% accuracy

---

## 🏗️ Service Architecture

### Services Overview:

| Service Name | Port | Database | Primary Function |
|-------------|------|----------|------------------|
| risk-scoring-service | 8094 | healthfinance_risk_scoring | Real-time patient risk stratification |
| care-pathway-service | 8095 | healthfinance_care_pathways | Disease-specific care pathways |
| triage-service | 8096 | healthfinance_triage | Symptom checker + routing |
| provider-service | 8099 | healthfinance_providers | Provider directory + scheduling |
| patient-engagement-service | 8102 | healthfinance_engagement | Engagement tracking + interventions |
| pricing-service | 8098 | healthfinance_pricing | Cost transparency + estimates |
| fraud-detection-service | 8097 | healthfinance_fraud | Claims fraud detection |
| claims-orchestrator-service | 8101 | healthfinance_claims | Temporal workflows |
| document-ai-service | 8103 | healthfinance_documents | OCR + extraction + summarization |

---

## 🎯 Service 1: Risk Scoring Service (Port 8094)

### Purpose:
Real-time calculation of patient risk scores across multiple risk types.

### Risk Types Supported:

1. **30-Day Readmission Risk**
   - Uses ML model: readmission-risk-v3
   - Input: Demographics, diagnosis, comorbidities, social factors
   - Output: Risk score 0.00-1.00, category (LOW/MODERATE/HIGH)

2. **Hospital-Acquired Infection Risk**
   - Uses ML model: hai-risk-v1
   - Input: Procedure type, length of stay, immune status
   - Output: Risk score + preventive measures

3. **Falls Risk (Elderly Patients)**
   - Uses ML model: falls-risk-v1
   - Input: Age, medications, mobility, cognitive status
   - Output: Risk category + prevention plan

4. **Sepsis Early Warning**
   - Uses ML model: sepsis-prediction-v2
   - Input: Vitals (HR, temp, WBC), lab trends
   - Output: Sepsis probability + urgency

5. **Medication Non-Adherence Risk**
   - Uses ML model: medication-adherence-v2
   - Input: Prescription complexity, refill history, demographics
   - Output: Adherence probability

6. **Emergency Department Utilization Risk**
   - Uses ML model: ed-utilization-v1
   - Input: Prior ED visits, chronic conditions, access to care
   - Output: Probability of ED visit next 30 days

7. **Disease Progression Risk**
   - Uses condition-specific models
   - Input: Condition history, biomarkers, treatment response
   - Output: Progression probability

### API Endpoints:

**POST /api/risk/calculate**
```json
Request:
{
  "patientId": "uuid",
  "riskType": "30_DAY_READMISSION",
  "context": {
    "diagnosis": "Heart Failure Exacerbation",
    "lengthOfStay": 5,
    "priorAdmissions": 2,
    "comorbidities": ["Diabetes", "CKD Stage 3"],
    "socialFactors": {
      "livesSinglehandedly": true,
      "transportAccess": false
    },
    "labResults": {
      "BNP": 850,
      "creatinine": 1.8
    }
  }
}

Response:
{
  "riskScoreId": "risk-uuid-123",
  "patientId": "uuid",
  "riskType": "30_DAY_READMISSION",
  "riskScore": 0.68,
  "riskCategory": "HIGH",
  "calculatedAt": "2026-01-08T10:00:00Z",
  "validUntil": "2026-01-15T10:00:00Z",
  "interventionsRecommended": [
    {"intervention": "Home health visits 2x/week", "impactEstimate": "-15%"},
    {"intervention": "Daily weight monitoring", "impactEstimate": "-10%"},
    {"intervention": "Medication reconciliation", "impactEstimate": "-8%"}
  ],
  "explanation": {
    "factors": [
      {"factor": "Prior readmissions (2)", "impact": 0.25},
      {"factor": "Lives alone", "impact": 0.20}
    ]
  },
  "carePathwayTriggered": "heart-failure-post-discharge"
}
```

**GET /api/risk/patient/{patientId}**
- Returns all active risk scores for a patient

**GET /api/risk/patient/{patientId}/type/{riskType}**
- Returns specific risk type for a patient

**GET /api/risk/high-risk-patients**
```json
Query Params:
- riskType
- riskCategory (HIGH, CRITICAL)
- facility
- assignedProvider
- page, size, sort

Response:
{
  "patients": [
    {
      "patientId": "uuid",
      "patientName": "John Doe",
      "mrn": "MRN-123456",
      "riskType": "30_DAY_READMISSION",
      "riskScore": 0.72,
      "riskCategory": "HIGH",
      "lastCalculated": "2026-01-08T09:00:00Z",
      "interventionsActive": 2,
      "assignedCareManager": "Jane Smith RN"
    },
    ...
  ],
  "totalElements": 45,
  "totalPages": 5
}
```

**POST /api/risk/batch-calculate**
- Calculate risk scores for multiple patients (batch processing)

### Database Schema:

**Table: risk_scores**
```sql
CREATE TABLE risk_scores (
    score_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    risk_type VARCHAR(100) NOT NULL,
    risk_score DECIMAL(5,4) NOT NULL,
    risk_category VARCHAR(50) NOT NULL,
    calculated_at TIMESTAMP NOT NULL,
    valid_until TIMESTAMP,
    contributing_factors JSONB,
    interventions_recommended JSONB,
    model_version VARCHAR(50),
    triggered_pathway VARCHAR(100),
    CONSTRAINT chk_risk_score CHECK (risk_score >= 0 AND risk_score <= 1)
);

CREATE INDEX idx_risk_patient ON risk_scores(patient_id);
CREATE INDEX idx_risk_type ON risk_scores(risk_type);
CREATE INDEX idx_risk_category ON risk_scores(risk_category);
CREATE INDEX idx_risk_calculated_at ON risk_scores(calculated_at);
```

### Integration Points:
- **Intelligence Hub:** Calls ML models for risk calculation
- **Care Pathway Service:** Triggers pathways based on risk scores
- **Patient Engagement Service:** Triggers interventions for high-risk patients
- **Notification Service:** Sends alerts to care team for critical risks

### Events Published:
- RiskScoreCalculatedEvent
- HighRiskPatientIdentifiedEvent
- RiskScoreExpiredEvent

---

## 🛤️ Service 2: Care Pathway Service (Port 8095)

### Purpose:
Manage disease-specific care pathways with automated task assignment and progress tracking.

### Pathway Templates (Phase 3 MVP - 5 Pathways):

#### 1. Type 2 Diabetes Management (12-month pathway)

**Stages:**
1. **Diagnosis & Education (Month 1)**
   - Tasks: HbA1c baseline, lipid panel, kidney function, eye exam referral
   - Patient education: Diabetes basics, diet, exercise
   - Medication initiation (if needed)

2. **Medication Titration (Months 2-3)**
   - Tasks: Weekly glucose monitoring, medication adjustments
   - Patient education: Medication adherence, hypoglycemia recognition
   - Follow-up appointment

3. **Optimization (Months 4-6)**
   - Tasks: HbA1c recheck, foot exam, retinal screening
   - Patient education: Complication prevention
   - Lifestyle coaching

4. **Maintenance (Months 7-12)**
   - Tasks: Quarterly HbA1c, annual comprehensive exam
   - Patient education: Long-term management
   - Complication screening

**Milestones:**
- HbA1c <7.0% achieved
- All screening exams completed
- Medication adherence >80%

#### 2. Hypertension Control (6-month pathway)

**Stages:**
1. **Initial Assessment (Week 1)**
   - Tasks: BP monitoring (3 readings), lab work (kidney function, electrolytes)
   - Patient education: BP monitoring technique, lifestyle modifications

2. **Medication Titration (Weeks 2-8)**
   - Tasks: Weekly BP checks, medication adjustments
   - Patient education: Medication adherence, side effects

3. **Stabilization (Months 3-6)**
   - Tasks: Monthly BP monitoring
   - Patient education: Long-term adherence
   - Lifestyle reinforcement

**Milestones:**
- BP <130/80 achieved
- Medication stable
- Home BP monitoring established

#### 3. Heart Failure Post-Discharge (90-day pathway)

**Stages:**
1. **Immediate Post-Discharge (Days 1-7)**
   - Tasks: Daily weight monitoring, daily check-in calls
   - Patient education: Fluid restriction, symptom recognition
   - Medication reconciliation
   - Follow-up appointment within 7 days

2. **Early Recovery (Weeks 2-4)**
   - Tasks: 2x/week weight checks, weekly nurse visits
   - Patient education: Activity progression, diet
   - Medication titration

3. **Stabilization (Weeks 5-12)**
   - Tasks: Weekly weight monitoring, monthly follow-ups
   - Patient education: Long-term management
   - Cardiac rehab referral

**Milestones:**
- No readmissions
- Weight stable
- Functional capacity improved

#### 4. Asthma Control (6-month pathway)

**Stages:**
1. **Assessment & Action Plan (Week 1)**
   - Tasks: Spirometry, allergy testing, asthma action plan creation
   - Patient education: Inhaler technique, trigger avoidance

2. **Medication Optimization (Weeks 2-8)**
   - Tasks: Weekly symptom check-ins, peak flow monitoring
   - Patient education: Medication adherence
   - Inhaler technique review

3. **Maintenance (Months 3-6)**
   - Tasks: Monthly asthma control test (ACT), peak flow trends
   - Patient education: Long-term control
   - Environmental control

**Milestones:**
- ACT score >20 (well-controlled)
- No exacerbations
- Proper inhaler technique

#### 5. Depression Treatment (6-month pathway)

**Stages:**
1. **Initial Treatment (Weeks 1-4)**
   - Tasks: PHQ-9 baseline, safety assessment, medication initiation
   - Patient education: Depression basics, treatment options
   - Therapy referral

2. **Early Response (Weeks 5-12)**
   - Tasks: Weekly PHQ-9, medication titration, therapy attendance
   - Patient education: Side effect management
   - Crisis plan review

3. **Continuation (Months 4-6)**
   - Tasks: Monthly PHQ-9, medication maintenance
   - Patient education: Relapse prevention
   - Therapy ongoing

**Milestones:**
- PHQ-9 <10 (remission)
- No self-harm ideation
- Engaged in therapy

### API Endpoints:

**POST /api/pathways**
```json
Request:
{
  "templateId": "diabetes-type2-pathway",
  "patientId": "uuid",
  "initiatedBy": "provider-uuid",
  "customizations": {
    "skipStages": [],
    "additionalTasks": [],
    "targetHbA1c": 7.0
  }
}

Response:
{
  "pathwayId": "pathway-uuid-123",
  "templateId": "diabetes-type2-pathway",
  "patientId": "uuid",
  "status": "ACTIVE",
  "currentStage": "Diagnosis & Education",
  "initiatedDate": "2026-01-08T10:00:00Z",
  "expectedCompletion": "2027-01-08T10:00:00Z",
  "progressScore": 0.05,
  "nextTasks": [
    {
      "taskId": "task-uuid-1",
      "taskType": "LAB_ORDER",
      "description": "Order HbA1c, lipid panel, kidney function",
      "dueDate": "2026-01-10T23:59:59Z",
      "assignedTo": "provider-uuid"
    },
    {
      "taskId": "task-uuid-2",
      "taskType": "PATIENT_EDUCATION",
      "description": "Complete diabetes basics module",
      "dueDate": "2026-01-15T23:59:59Z",
      "assignedTo": "patient-uuid"
    }
  ]
}
```

**GET /api/pathways/{pathwayId}**
- Get pathway details and status

**POST /api/pathways/{pathwayId}/stages/{stageId}/complete**
```json
Request:
{
  "completedBy": "provider-uuid",
  "notes": "All tasks completed, patient progressing well"
}

Response:
{
  "pathwayId": "pathway-uuid-123",
  "currentStage": "Medication Titration",
  "progressScore": 0.25,
  "stageCompletedAt": "2026-02-08T10:00:00Z"
}
```

**GET /api/pathways/patient/{patientId}**
- Get all active pathways for a patient

**GET /api/pathways/templates**
```json
Response:
{
  "templates": [
    {
      "templateId": "diabetes-type2-pathway",
      "name": "Type 2 Diabetes Management",
      "illnessArea": "Metabolic",
      "durationDays": 365,
      "stages": 4,
      "totalTasks": 28,
      "aiEnabled": true
    },
    ...
  ]
}
```

**POST /api/pathways/{pathwayId}/tasks/{taskId}/complete**
- Mark task as complete

**GET /api/pathways/overdue-tasks**
- Get all overdue tasks across patients

### Database Schema:

**Table: pathway_templates**
```sql
CREATE TABLE pathway_templates (
    template_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    illness_area VARCHAR(100) NOT NULL,
    duration_days INTEGER,
    stages JSONB NOT NULL,
    tasks JSONB NOT NULL,
    ai_enabled BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER NOT NULL
);
```

**Table: active_pathways**
```sql
CREATE TABLE active_pathways (
    pathway_id UUID PRIMARY KEY,
    template_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    current_stage VARCHAR(100),
    initiated_date TIMESTAMP NOT NULL,
    expected_completion TIMESTAMP,
    actual_completion TIMESTAMP,
    progress_score DECIMAL(3,2),
    ai_recommendations JSONB,
    customizations JSONB,
    CONSTRAINT fk_template FOREIGN KEY (template_id) REFERENCES pathway_templates(template_id)
);

CREATE INDEX idx_pathways_patient ON active_pathways(patient_id);
CREATE INDEX idx_pathways_status ON active_pathways(status);
```

**Table: pathway_tasks**
```sql
CREATE TABLE pathway_tasks (
    task_id UUID PRIMARY KEY,
    pathway_id UUID NOT NULL,
    stage_name VARCHAR(100),
    task_type VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    due_date TIMESTAMP,
    assigned_to UUID,
    assigned_role VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    completed_at TIMESTAMP,
    completed_by UUID,
    notes TEXT,
    CONSTRAINT fk_pathway FOREIGN KEY (pathway_id) REFERENCES active_pathways(pathway_id) ON DELETE CASCADE
);

CREATE INDEX idx_tasks_pathway ON pathway_tasks(pathway_id);
CREATE INDEX idx_tasks_assigned_to ON pathway_tasks(assigned_to);
CREATE INDEX idx_tasks_status ON pathway_tasks(status);
CREATE INDEX idx_tasks_due_date ON pathway_tasks(due_date);
```

### Integration Points:
- **Risk Scoring Service:** Pathways triggered by high risk scores
- **Intelligence Hub:** AI recommendations for pathway adjustments
- **Notification Service:** Task reminders and milestone notifications
- **Patient Engagement Service:** Track pathway adherence

### Events Published:
- PathwayInitiatedEvent
- PathwayStageCompletedEvent
- PathwayMilestoneReachedEvent
- PathwayCompletedEvent
- TaskOverdueEvent

---

## 🚑 Service 3: Triage Service (Port 8096)

### Purpose:
AI-powered symptom triage, severity classification, and routing.

### Features:

1. **Symptom Checker**
   - Natural language processing of patient-described symptoms
   - Symptom encoding and normalization
   - Related symptom suggestions

2. **Severity Classification**
   - Uses ML model: triage-severity-v2
   - Output: EMERGENCY, URGENT, SOON, ROUTINE, SELF_CARE

3. **Specialty Routing**
   - Recommend primary care vs specialist
   - Suggest specialty (cardiology, dermatology, etc.)

4. **Red Flag Detection**
   - Immediate escalation for life-threatening symptoms
   - Examples: Chest pain, stroke symptoms, severe bleeding

### API Endpoints:

**POST /api/triage/assess**
```json
Request:
{
  "patientId": "uuid",
  "chiefComplaint": "Chest pain and shortness of breath",
  "symptoms": [
    {"symptom": "Chest pain", "severity": 8, "duration": "30 minutes", "onset": "sudden"},
    {"symptom": "Shortness of breath", "severity": 7, "duration": "30 minutes"}
  ],
  "vitals": {
    "heartRate": 110,
    "bloodPressure": "145/95",
    "temperature": 37.2,
    "oxygenSaturation": 94
  },
  "patientContext": {
    "age": 65,
    "gender": "M",
    "medicalHistory": ["Hypertension", "High Cholesterol"],
    "medications": ["Lisinopril", "Atorvastatin"]
  }
}

Response:
{
  "assessmentId": "triage-uuid-123",
  "severity": "EMERGENCY",
  "urgencyScore": 0.95,
  "recommendedAction": "CALL_AMBULANCE",
  "reasoning": "RED FLAGS: Chest pain + shortness of breath + cardiac risk factors → possible MI",
  "triageLevel": "RESUS",
  "estimatedWaitTime": "0 minutes",
  "specialtyRecommendation": "Emergency Medicine / Cardiology",
  "redFlags": [
    "Chest pain lasting >20 minutes",
    "Associated shortness of breath",
    "Age >60 with cardiac risk factors"
  ],
  "differentialDiagnosis": [
    {"condition": "Myocardial Infarction", "probability": 0.65},
    {"condition": "Unstable Angina", "probability": 0.20},
    {"condition": "Pulmonary Embolism", "probability": 0.10}
  ],
  "explanation": {
    "factors": [
      {"factor": "Chest pain severity", "impact": 0.50},
      {"factor": "Age + cardiac history", "impact": 0.30}
    ]
  },
  "createdAt": "2026-01-08T14:35:00Z"
}
```

**POST /api/triage/symptoms/check**
```json
Request:
{
  "symptomText": "headache and dizziness for 3 days"
}

Response:
{
  "extractedSymptoms": [
    {"symptom": "Headache", "confidence": 0.98},
    {"symptom": "Dizziness", "confidence": 0.95}
  ],
  "relatedSymptoms": [
    "Nausea",
    "Blurred vision",
    "Light sensitivity"
  ],
  "questionsToAsk": [
    "How severe is the headache (1-10)?",
    "Did the headache start suddenly or gradually?",
    "Have you had any recent head injuries?"
  ]
}
```

**GET /api/triage/{assessmentId}**
- Retrieve triage assessment details

### Database Schema:

**Table: triage_assessments**
```sql
CREATE TABLE triage_assessments (
    assessment_id UUID PRIMARY KEY,
    patient_id UUID,
    chief_complaint TEXT,
    symptoms JSONB NOT NULL,
    vitals JSONB,
    severity VARCHAR(50) NOT NULL,
    urgency_score DECIMAL(5,4),
    recommended_action VARCHAR(100),
    triage_level VARCHAR(50),
    specialty_recommendation VARCHAR(100),
    red_flags JSONB,
    differential_diagnosis JSONB,
    model_version VARCHAR(50),
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_triage_patient ON triage_assessments(patient_id);
CREATE INDEX idx_triage_severity ON triage_assessments(severity);
CREATE INDEX idx_triage_created_at ON triage_assessments(created_at);
```

### Integration Points:
- **Intelligence Hub:** Uses triage-severity-v2 model
- **Provider Service:** Routes to appropriate specialists
- **Appointment Service:** Schedules urgent appointments
- **Notification Service:** Sends triage results to patient

---

## 👨‍⚕️ Service 4: Provider Service (Port 8099)

### Purpose:
Healthcare provider directory, search, scheduling, and reviews.

### Features:

1. **Provider Directory**
   - Provider profiles (NPI, specialty, credentials)
   - Practice locations
   - Accepted insurance
   - Languages spoken

2. **Provider Search**
   - Search by specialty, location, name
   - Filter by insurance, availability, ratings
   - Distance-based search (geospatial)

3. **Availability Management**
   - Provider schedules
   - Available appointment slots
   - Time zone handling

4. **Provider Reviews**
   - Patient ratings (1-5 stars)
   - Written reviews
   - Aggregate ratings

### API Endpoints:

**POST /api/providers**
```json
Request:
{
  "npi": "1234567890",
  "firstName": "Jane",
  "lastName": "Smith",
  "specialty": "Cardiology",
  "subspecialties": ["Interventional Cardiology"],
  "licenseState": "CA",
  "licenseNumber": "A123456",
  "education": [
    {"degree": "MD", "institution": "Stanford Medical School", "year": 2005}
  ],
  "certifications": [
    {"name": "Board Certified - Internal Medicine", "issuingBody": "ABIM", "year": 2008},
    {"name": "Board Certified - Cardiovascular Disease", "issuingBody": "ABIM", "year": 2011}
  ],
  "languages": ["English", "Spanish"],
  "acceptingNewPatients": true,
  "practiceLocations": [
    {
      "facilityName": "City Heart Center",
      "address": {
        "street": "123 Main St",
        "city": "San Francisco",
        "state": "CA",
        "zip": "94102"
      },
      "phone": "(415) 555-1234",
      "coordinates": {
        "latitude": 37.7749,
        "longitude": -122.4194
      }
    }
  ],
  "acceptedInsurance": ["Blue Cross", "Aetna", "United Healthcare"]
}

Response:
{
  "providerId": "provider-uuid-123",
  "npi": "1234567890",
  "fullName": "Jane Smith, MD",
  "specialty": "Cardiology",
  "rating": null,
  "reviewCount": 0,
  "createdAt": "2026-01-08T10:00:00Z"
}
```

**GET /api/providers**
```json
Query Params:
- specialty
- location (city, state, zip)
- radius (miles)
- insurance
- language
- acceptingNewPatients (true/false)
- minRating (1-5)
- page, size, sort

Response:
{
  "providers": [
    {
      "providerId": "provider-uuid-123",
      "fullName": "Jane Smith, MD",
      "specialty": "Cardiology",
      "subspecialties": ["Interventional Cardiology"],
      "rating": 4.8,
      "reviewCount": 156,
      "acceptingNewPatients": true,
      "nextAvailableAppointment": "2026-01-12T09:00:00Z",
      "distance": 2.3,
      "practiceLocation": {
        "facilityName": "City Heart Center",
        "address": "123 Main St, San Francisco, CA 94102",
        "phone": "(415) 555-1234"
      }
    },
    ...
  ],
  "totalElements": 23,
  "totalPages": 3
}
```

**GET /api/providers/{providerId}**
- Get detailed provider profile

**GET /api/providers/{providerId}/availability**
```json
Query Params:
- startDate (ISO 8601)
- endDate (ISO 8601)
- appointmentType (NEW_PATIENT, FOLLOW_UP, URGENT)

Response:
{
  "providerId": "provider-uuid-123",
  "availableSlots": [
    {
      "slotId": "slot-uuid-1",
      "startTime": "2026-01-12T09:00:00Z",
      "endTime": "2026-01-12T09:30:00Z",
      "appointmentType": "NEW_PATIENT",
      "location": "City Heart Center"
    },
    {
      "slotId": "slot-uuid-2",
      "startTime": "2026-01-12T09:30:00Z",
      "endTime": "2026-01-12T10:00:00Z",
      "appointmentType": "NEW_PATIENT",
      "location": "City Heart Center"
    },
    ...
  ]
}
```

**POST /api/providers/{providerId}/reviews**
```json
Request:
{
  "patientId": "patient-uuid",
  "rating": 5,
  "reviewText": "Dr. Smith was excellent. Very thorough and caring.",
  "appointmentDate": "2026-01-05"
}

Response:
{
  "reviewId": "review-uuid-123",
  "providerId": "provider-uuid-123",
  "rating": 5,
  "reviewText": "Dr. Smith was excellent. Very thorough and caring.",
  "createdAt": "2026-01-08T10:00:00Z",
  "verified": true
}
```

**GET /api/providers/{providerId}/reviews**
- Get all reviews for a provider

### Database Schema:

**Table: providers**
```sql
CREATE TABLE providers (
    provider_id UUID PRIMARY KEY,
    npi VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    specialty VARCHAR(100),
    subspecialties TEXT[],
    license_state VARCHAR(2),
    license_number VARCHAR(50),
    accepting_new_patients BOOLEAN DEFAULT true,
    languages TEXT[],
    education JSONB,
    certifications JSONB,
    rating DECIMAL(2,1),
    review_count INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_providers_specialty ON providers(specialty);
CREATE INDEX idx_providers_accepting ON providers(accepting_new_patients);
CREATE INDEX idx_providers_rating ON providers(rating);
```

**Table: practice_locations**
```sql
CREATE TABLE practice_locations (
    location_id UUID PRIMARY KEY,
    provider_id UUID NOT NULL,
    facility_name VARCHAR(255),
    address JSONB NOT NULL,
    phone VARCHAR(20),
    coordinates GEOGRAPHY(POINT, 4326),
    CONSTRAINT fk_provider FOREIGN KEY (provider_id) REFERENCES providers(provider_id) ON DELETE CASCADE
);

CREATE INDEX idx_locations_provider ON practice_locations(provider_id);
CREATE INDEX idx_locations_coordinates ON practice_locations USING GIST(coordinates);
```

**Table: provider_availability**
```sql
CREATE TABLE provider_availability (
    availability_id UUID PRIMARY KEY,
    provider_id UUID NOT NULL,
    location_id UUID,
    day_of_week INTEGER NOT NULL, -- 0=Sunday, 6=Saturday
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    appointment_duration INTEGER NOT NULL, -- minutes
    CONSTRAINT fk_provider FOREIGN KEY (provider_id) REFERENCES providers(provider_id) ON DELETE CASCADE,
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES practice_locations(location_id)
);
```

**Table: provider_reviews**
```sql
CREATE TABLE provider_reviews (
    review_id UUID PRIMARY KEY,
    provider_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    appointment_date DATE,
    verified BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_provider FOREIGN KEY (provider_id) REFERENCES providers(provider_id) ON DELETE CASCADE
);

CREATE INDEX idx_reviews_provider ON provider_reviews(provider_id);
CREATE INDEX idx_reviews_rating ON provider_reviews(rating);
```

### Integration Points:
- **Appointment Service:** Book appointments with providers
- **Triage Service:** Route patients to appropriate specialists
- **Identity Service:** Provider user accounts

---

## 📊 Service 5: Patient Engagement Service (Port 8102)

### Purpose:
Track patient engagement, trigger interventions, and optimize communication.

### Engagement Metrics Tracked:

1. **Portal Login Frequency**
2. **Appointment Attendance Rate**
3. **Medication Adherence**
4. **Symptom Check-in Completion**
5. **Survey Response Rate**
6. **Educational Content Consumption**
7. **Care Plan Task Completion**

### Engagement Score Calculation:
```
Engagement Score = weighted average of metrics (0.00 to 1.00)

Weights:
- Appointment attendance: 0.25
- Medication adherence: 0.25
- Check-in completion: 0.20
- Portal usage: 0.15
- Task completion: 0.15
```

### API Endpoints:

**GET /api/engagement/patient/{patientId}/score**
```json
Response:
{
  "patientId": "patient-uuid",
  "engagementScore": 0.72,
  "engagementCategory": "MODERATE",
  "trend": "IMPROVING",
  "metrics": {
    "portalLogins": {
      "last30Days": 12,
      "score": 0.80
    },
    "appointmentAttendance": {
      "last6Months": 0.85,
      "score": 0.85
    },
    "medicationAdherence": {
      "current": 0.78,
      "score": 0.78
    },
    "checkinCompletion": {
      "last30Days": 0.60,
      "score": 0.60
    },
    "surveyResponse": {
      "last3Months": 0.50,
      "score": 0.50
    }
  },
  "calculatedAt": "2026-01-08T10:00:00Z"
}
```

**POST /api/engagement/interventions**
```json
Request:
{
  "patientId": "patient-uuid",
  "interventionType": "APPOINTMENT_REMINDER",
  "channel": "SMS",
  "content": "Reminder: You have an appointment tomorrow at 2pm with Dr. Smith",
  "scheduledFor": "2026-01-09T12:00:00Z"
}

Response:
{
  "interventionId": "intervention-uuid-123",
  "status": "SCHEDULED",
  "scheduledFor": "2026-01-09T12:00:00Z"
}
```

**GET /api/engagement/low-engagement**
```json
Query Params:
- threshold (default 0.4)
- facilityId
- providerId
- conditionType
- page, size, sort

Response:
{
  "patients": [
    {
      "patientId": "patient-uuid",
      "patientName": "John Doe",
      "engagementScore": 0.32,
      "trend": "DECLINING",
      "lastPortalLogin": "2025-12-15T08:00:00Z",
      "missedAppointments": 2,
      "medicationAdherence": 0.45,
      "riskFactors": [
        "Recent diagnosis",
        "Lives alone",
        "Low health literacy"
      ],
      "recommendedInterventions": [
        "Personal outreach call",
        "Simplify medication regimen",
        "Home health visit"
      ]
    },
    ...
  ],
  "totalElements": 87
}
```

**POST /api/engagement/events**
```json
Request:
{
  "patientId": "patient-uuid",
  "eventType": "PORTAL_LOGIN",
  "timestamp": "2026-01-08T10:00:00Z",
  "metadata": {
    "duration": 320,
    "pagesViewed": 5
  }
}

Response:
{
  "eventId": "event-uuid-123",
  "recorded": true
}
```

### Database Schema:

**Table: engagement_scores**
```sql
CREATE TABLE engagement_scores (
    score_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    engagement_score DECIMAL(3,2) NOT NULL,
    engagement_category VARCHAR(50),
    trend VARCHAR(50),
    metrics JSONB NOT NULL,
    calculated_at TIMESTAMP NOT NULL,
    CONSTRAINT chk_engagement_score CHECK (engagement_score >= 0 AND engagement_score <= 1)
);

CREATE INDEX idx_engagement_patient ON engagement_scores(patient_id);
CREATE INDEX idx_engagement_score ON engagement_scores(engagement_score);
CREATE INDEX idx_engagement_calculated_at ON engagement_scores(calculated_at);
```

**Table: engagement_events**
```sql
CREATE TABLE engagement_events (
    event_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    metadata JSONB
);

CREATE INDEX idx_events_patient ON engagement_events(patient_id);
CREATE INDEX idx_events_type ON engagement_events(event_type);
CREATE INDEX idx_events_timestamp ON engagement_events(timestamp);
```

**Table: engagement_interventions**
```sql
CREATE TABLE engagement_interventions (
    intervention_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    intervention_type VARCHAR(100) NOT NULL,
    channel VARCHAR(50),
    content TEXT,
    scheduled_for TIMESTAMP,
    sent_at TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    response_received BOOLEAN DEFAULT false
);

CREATE INDEX idx_interventions_patient ON engagement_interventions(patient_id);
CREATE INDEX idx_interventions_scheduled ON engagement_interventions(scheduled_for);
CREATE INDEX idx_interventions_status ON engagement_interventions(status);
```

### Integration Points:
- **Notification Service:** Send engagement interventions
- **Care Pathway Service:** Track pathway task completion
- **Appointment Service:** Track attendance

---

## 💰 Service 6: Pricing Service (Port 8098)

### Purpose:
Provide cost transparency, cost estimates, and treatment cost comparisons.

### Features:

1. **Procedure Cost Estimation**
   - Based on CPT codes, facility, and insurance
   - Include facility fee, professional fee, anesthesia

2. **Insurance Coverage Prediction**
   - ML model predicts coverage based on policy + procedure
   - Estimate patient out-of-pocket cost

3. **Alternative Treatment Comparison**
   - Compare costs of different treatment options
   - Include effectiveness and risks

4. **Regional Pricing Benchmarking**
   - Show regional cost variations
   - Highlight cost-effective providers

### API Endpoints:

**POST /api/pricing/estimate**
```json
Request:
{
  "patientId": "patient-uuid",
  "procedureCodes": ["99213", "36415"], // CPT codes
  "facilityId": "facility-uuid",
  "providerId": "provider-uuid",
  "insurancePolicyId": "policy-uuid"
}

Response:
{
  "estimateId": "estimate-uuid-123",
  "procedures": [
    {
      "cptCode": "99213",
      "description": "Office visit, established patient, 20-29 min",
      "facilityFee": 150.00,
      "professionalFee": 100.00,
      "totalCost": 250.00
    },
    {
      "cptCode": "36415",
      "description": "Routine venipuncture",
      "facilityFee": 0.00,
      "professionalFee": 15.00,
      "totalCost": 15.00
    }
  ],
  "totalEstimatedCost": 265.00,
  "insuranceCoverage": {
    "policyId": "policy-uuid",
    "payer": "Blue Cross",
    "estimatedCoverage": 212.00,
    "patientResponsibility": {
      "copay": 30.00,
      "coinsurance": 23.00,
      "deductible": 0.00,
      "total": 53.00
    },
    "confidence": 0.85
  },
  "regionalComparison": {
    "averageCost": 280.00,
    "percentile": 45,
    "savingsVsAverage": 15.00
  },
  "createdAt": "2026-01-08T10:00:00Z"
}
```

**POST /api/pricing/compare**
```json
Request:
{
  "patientId": "patient-uuid",
  "condition": "Diabetes Type 2",
  "treatmentOptions": [
    {
      "name": "Metformin (generic)",
      "cptCodes": ["99213"],
      "medicationCost": 10.00
    },
    {
      "name": "GLP-1 Agonist (Ozempic)",
      "cptCodes": ["99213"],
      "medicationCost": 900.00
    }
  ]
}

Response:
{
  "comparisons": [
    {
      "treatmentName": "Metformin (generic)",
      "monthlyCost": 30.00,
      "yearlyCost": 360.00,
      "effectiveness": "Moderate",
      "sideEffects": "Mild GI upset",
      "insuranceCoverage": "Full coverage"
    },
    {
      "treatmentName": "GLP-1 Agonist (Ozempic)",
      "monthlyCost": 950.00,
      "yearlyCost": 11400.00,
      "effectiveness": "High + weight loss",
      "sideEffects": "Nausea, injection site",
      "insuranceCoverage": "Prior authorization required"
    }
  ],
  "recommendation": "Start with Metformin, escalate to GLP-1 if inadequate response"
}
```

**GET /api/pricing/patient/{patientId}/coverage**
- Get patient's insurance coverage details

### Database Schema:

**Table: pricing_estimates**
```sql
CREATE TABLE pricing_estimates (
    estimate_id UUID PRIMARY KEY,
    patient_id UUID,
    procedure_codes TEXT[] NOT NULL,
    facility_id UUID,
    provider_id UUID,
    total_estimated_cost DECIMAL(10,2),
    insurance_coverage JSONB,
    patient_responsibility DECIMAL(10,2),
    regional_comparison JSONB,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_estimates_patient ON pricing_estimates(patient_id);
```

**Table: procedure_pricing**
```sql
CREATE TABLE procedure_pricing (
    pricing_id UUID PRIMARY KEY,
    cpt_code VARCHAR(10) NOT NULL,
    description TEXT,
    facility_id UUID,
    facility_fee DECIMAL(10,2),
    professional_fee DECIMAL(10,2),
    effective_date DATE NOT NULL,
    expiration_date DATE
);

CREATE INDEX idx_pricing_cpt ON procedure_pricing(cpt_code);
CREATE INDEX idx_pricing_facility ON procedure_pricing(facility_id);
```

---

## 🕵️ Service 7: Fraud Detection Service (Port 8097)

### Purpose:
Detect fraudulent insurance claims using ML and rule-based detection.

### Detection Patterns:

1. **Duplicate Billing**
   - Same procedure billed multiple times for same date
2. **Upcoding**
   - Billing higher-level code than service provided
3. **Unbundling**
   - Billing components separately when bundled code exists
4. **Medical Necessity Violations**
   - Procedures not medically necessary for diagnosis
5. **Provider-Patient Relationship Anomalies**
   - Unusual patient volumes, geographic anomalies

### API Endpoints:

**POST /api/fraud/analyze-claim**
```json
Request:
{
  "claimId": "claim-uuid-123",
  "providerId": "provider-uuid",
  "patientId": "patient-uuid",
  "diagnosisCodes": ["E11.9"], // Type 2 Diabetes
  "procedureCodes": ["99215"], // High-level office visit
  "claimAmount": 500.00,
  "serviceDate": "2026-01-05",
  "submittedDate": "2026-01-08"
}

Response:
{
  "analysisId": "fraud-analysis-uuid-123",
  "claimId": "claim-uuid-123",
  "fraudProbability": 0.72,
  "riskCategory": "HIGH",
  "flags": [
    {
      "flagType": "UPCODING",
      "severity": "HIGH",
      "description": "99215 (high complexity) may be upcoded - average for this provider is 99213",
      "recommendation": "Review documentation for complexity justification"
    },
    {
      "flagType": "BILLING_FREQUENCY",
      "severity": "MODERATE",
      "description": "Provider billing frequency 2.5x higher than peers in same specialty",
      "recommendation": "Audit provider billing patterns"
    }
  ],
  "anomalyScores": {
    "claimAmount": 0.85,
    "procedureFrequency": 0.65,
    "providerVolume": 0.80
  },
  "recommendedAction": "MANUAL_REVIEW",
  "explanation": {
    "factors": [
      {"factor": "High-level visit code", "impact": 0.40},
      {"factor": "Provider billing patterns", "impact": 0.30},
      {"factor": "Claim amount vs average", "impact": 0.30}
    ]
  },
  "createdAt": "2026-01-08T10:00:00Z"
}
```

**GET /api/fraud/alerts**
```json
Query Params:
- riskCategory (HIGH, CRITICAL)
- status (PENDING, INVESTIGATING, RESOLVED)
- page, size, sort

Response:
{
  "alerts": [
    {
      "alertId": "alert-uuid-123",
      "claimId": "claim-uuid-456",
      "providerId": "provider-uuid",
      "fraudProbability": 0.85,
      "riskCategory": "CRITICAL",
      "primaryFlag": "DUPLICATE_BILLING",
      "potentialSavings": 1500.00,
      "status": "PENDING",
      "createdAt": "2026-01-08T09:00:00Z"
    },
    ...
  ]
}
```

**POST /api/fraud/alerts/{alertId}/resolve**
```json
Request:
{
  "resolution": "CONFIRMED_FRAUD",
  "notes": "Provider admitted to billing error, refunding amount",
  "actionTaken": "REFUND_REQUESTED",
  "resolvedBy": "investigator-uuid"
}

Response:
{
  "alertId": "alert-uuid-123",
  "status": "RESOLVED",
  "resolution": "CONFIRMED_FRAUD",
  "resolvedAt": "2026-01-08T15:00:00Z"
}
```

### Database Schema:

**Table: fraud_analyses**
```sql
CREATE TABLE fraud_analyses (
    analysis_id UUID PRIMARY KEY,
    claim_id UUID NOT NULL,
    provider_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    fraud_probability DECIMAL(5,4),
    risk_category VARCHAR(50),
    flags JSONB,
    anomaly_scores JSONB,
    recommended_action VARCHAR(100),
    model_version VARCHAR(50),
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_fraud_claim ON fraud_analyses(claim_id);
CREATE INDEX idx_fraud_provider ON fraud_analyses(provider_id);
CREATE INDEX idx_fraud_risk ON fraud_analyses(risk_category);
```

**Table: fraud_alerts**
```sql
CREATE TABLE fraud_alerts (
    alert_id UUID PRIMARY KEY,
    analysis_id UUID NOT NULL,
    claim_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    resolution VARCHAR(100),
    notes TEXT,
    resolved_by UUID,
    resolved_at TIMESTAMP,
    CONSTRAINT fk_analysis FOREIGN KEY (analysis_id) REFERENCES fraud_analyses(analysis_id)
);

CREATE INDEX idx_alerts_status ON fraud_alerts(status);
```

---

## 🔄 Service 8: Claims Orchestrator Service (Port 8101)

### Purpose:
Orchestrate complex, long-running insurance claim workflows using Temporal.

### Workflows:

#### 1. Standard Claim Processing Workflow

**Duration:** 2-14 days

**Steps:**
1. **Validate Claim**
   - Check required fields
   - Validate patient eligibility
   - Validate provider credentials

2. **Fraud Check**
   - Call fraud-detection-service
   - If HIGH risk → Human review queue

3. **Coverage Calculation**
   - Call pricing-service
   - Determine coverage amount
   - Calculate patient responsibility

4. **Submit to Payer**
   - Format claim (EDI 837)
   - Submit to insurance company
   - Track submission

5. **Await Response**
   - Poll for response (EDI 835)
   - Timeout: 14 days

6. **Process Response**
   - If APPROVED → Update status, process payment
   - If DENIED → Create denial record, notify provider

#### 2. Prior Authorization Workflow

**Duration:** 1-3 days
**SLA:** 72 hours

**Steps:**
1. **Gather Documentation**
   - Clinical notes
   - Lab results
   - Imaging reports

2. **AI Review**
   - Call intelligence-hub decision service
   - Check medical necessity

3. **Human Review** (if needed)
   - If AI uncertain → Create review task
   - Reviewer approves/denies

4. **Submit PA Request**
   - Format request
   - Submit to payer

5. **Track PA Status**
   - Poll payer API
   - Escalate if approaching SLA deadline

6. **Process PA Decision**
   - If APPROVED → Proceed with claim
   - If DENIED → Notify provider, initiate appeal if requested

#### 3. Appeals Workflow

**Duration:** 14-60 days

**Steps:**
1. **Gather Supporting Evidence**
2. **Draft Appeal Letter** (AI-assisted)
3. **Provider Review & Sign**
4. **Submit Appeal**
5. **Track Appeal Status**
6. **Process Appeal Decision**

### Temporal Workflow Code Structure:

**Workflow Interface:**
```java
@WorkflowInterface
public interface ClaimProcessingWorkflow {
    @WorkflowMethod
    ClaimResult processClaimcompilation(ClaimRequest request);
}
```

**Workflow Implementation:**
```java
@WorkflowImpl(taskQueues = "claims-processing")
public class ClaimProcessingWorkflowImpl implements ClaimProcessingWorkflow {

    private final ClaimActivities activities = Workflow.newActivityStub(
        ClaimActivities.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(5))
            .build()
    );

    @Override
    public ClaimResult processClaim(ClaimRequest request) {
        // Step 1: Validate
        ValidationResult validation = activities.validateClaim(request);
        if (!validation.isValid()) {
            return ClaimResult.rejected(validation.getErrors());
        }

        // Step 2: Fraud check
        FraudAnalysis fraudAnalysis = activities.checkFraud(request);
        if (fraudAnalysis.getRiskCategory().equals("HIGH")) {
            HumanReviewResult humanReview = activities.requestHumanReview(fraudAnalysis);
            if (!humanReview.isApproved()) {
                return ClaimResult.rejected("Fraud detected");
            }
        }

        // Step 3: Coverage calculation
        CoverageResult coverage = activities.calculateCoverage(request);

        // Step 4: Submit to payer
        SubmissionResult submission = activities.submitToPayer(request, coverage);

        // Step 5: Wait for response (with timeout)
        PayerResponse response = Workflow.await(
            Duration.ofDays(14),
            () -> activities.checkPayerResponse(submission.getClaimId())
        );

        // Step 6: Process response
        return activities.processPayerResponse(response);
    }
}
```

### API Endpoints:

**POST /api/claims/workflows/start**
```json
Request:
{
  "workflowType": "STANDARD_CLAIM_PROCESSING",
  "claimId": "claim-uuid-123",
  "providerId": "provider-uuid",
  "patientId": "patient-uuid",
  "diagnosisCodes": ["E11.9"],
  "procedureCodes": ["99213"],
  "claimAmount": 250.00
}

Response:
{
  "workflowId": "workflow-uuid-123",
  "runId": "run-uuid-456",
  "status": "RUNNING",
  "startedAt": "2026-01-08T10:00:00Z"
}
```

**GET /api/claims/workflows/{workflowId}**
```json
Response:
{
  "workflowId": "workflow-uuid-123",
  "workflowType": "STANDARD_CLAIM_PROCESSING",
  "status": "RUNNING",
  "currentStep": "AWAITING_PAYER_RESPONSE",
  "startedAt": "2026-01-08T10:00:00Z",
  "elapsedTime": "2 days 5 hours",
  "estimatedCompletion": "2026-01-18T10:00:00Z",
  "history": [
    {"step": "VALIDATE_CLAIM", "status": "COMPLETED", "completedAt": "2026-01-08T10:05:00Z"},
    {"step": "FRAUD_CHECK", "status": "COMPLETED", "completedAt": "2026-01-08T10:10:00Z"},
    {"step": "CALCULATE_COVERAGE", "status": "COMPLETED", "completedAt": "2026-01-08T10:15:00Z"},
    {"step": "SUBMIT_TO_PAYER", "status": "COMPLETED", "completedAt": "2026-01-08T10:20:00Z"},
    {"step": "AWAIT_RESPONSE", "status": "IN_PROGRESS", "startedAt": "2026-01-08T10:20:00Z"}
  ]
}
```

**POST /api/claims/workflows/{workflowId}/cancel**
- Cancel running workflow

### Database Schema:

**Table: workflow_executions**
```sql
CREATE TABLE workflow_executions (
    execution_id UUID PRIMARY KEY,
    workflow_id VARCHAR(255) NOT NULL,
    run_id VARCHAR(255) NOT NULL,
    workflow_type VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    input JSONB NOT NULL,
    result JSONB,
    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    UNIQUE(workflow_id, run_id)
);

CREATE INDEX idx_executions_workflow_id ON workflow_executions(workflow_id);
CREATE INDEX idx_executions_status ON workflow_executions(status);
```

### Integration Points:
- **Fraud Detection Service:** Fraud checks during workflow
- **Pricing Service:** Coverage calculations
- **Intelligence Hub:** Medical necessity reviews
- **Notification Service:** Workflow status updates

---

## 📄 Service 9: Document AI Service (Port 8103)

### Purpose:
OCR, data extraction, and summarization of medical documents.

### Capabilities:

1. **OCR (Optical Character Recognition)**
   - PDF → Text
   - Image → Text
   - Technology: Tesseract OCR

2. **Medical Form Extraction**
   - Structured data extraction from forms
   - Technology: LayoutLM (Microsoft)

3. **Clinical Note Summarization**
   - Long clinical notes → concise summaries
   - Technology: GPT-based models (OpenAI API or open-source)

4. **ICD-10/CPT Code Extraction**
   - Extract diagnosis and procedure codes from text

5. **Lab Report Parsing**
   - Structured extraction of lab results

### API Endpoints:

**POST /api/document/extract**
```json
Request (multipart/form-data):
- file: (PDF or image file)
- documentType: (CLINICAL_NOTE, LAB_REPORT, PRESCRIPTION, INSURANCE_CARD)

Response:
{
  "documentId": "doc-uuid-123",
  "documentType": "LAB_REPORT",
  "extractedData": {
    "patientName": "John Doe",
    "dateOfService": "2026-01-05",
    "results": [
      {"test": "HbA1c", "value": "8.2", "unit": "%", "referenceRange": "4.0-5.6", "flag": "HIGH"},
      {"test": "Creatinine", "value": "1.3", "unit": "mg/dL", "referenceRange": "0.7-1.3", "flag": "NORMAL"}
    ]
  },
  "confidence": 0.95,
  "rawText": "Full OCR text...",
  "processedAt": "2026-01-08T10:00:00Z"
}
```

**POST /api/document/summarize**
```json
Request:
{
  "text": "Patient presented with chest pain... (long clinical note)",
  "summaryType": "BRIEF"
}

Response:
{
  "summary": "65yo M with h/o HTN, HLD presented with chest pain x 30min. EKG showed ST elevations in V2-V4. Troponin elevated at 5.2. Dx: STEMI. Rx: Emergent cath lab, aspirin, heparin.",
  "keyFindings": [
    "STEMI (ST-elevation myocardial infarction)",
    "Troponin 5.2 (significantly elevated)",
    "Emergent cardiac catheterization performed"
  ],
  "extractedCodes": {
    "icd10": ["I21.09"],
    "cpt": ["93458"]
  }
}
```

**POST /api/document/classify**
```json
Request (multipart/form-data):
- file: (PDF or image)

Response:
{
  "documentType": "LAB_REPORT",
  "confidence": 0.98
}
```

### Database Schema:

**Table: processed_documents**
```sql
CREATE TABLE processed_documents (
    document_id UUID PRIMARY KEY,
    patient_id UUID,
    document_type VARCHAR(100) NOT NULL,
    original_filename VARCHAR(255),
    file_path TEXT NOT NULL,
    extracted_data JSONB,
    raw_text TEXT,
    confidence DECIMAL(3,2),
    processed_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_documents_patient ON processed_documents(patient_id);
CREATE INDEX idx_documents_type ON processed_documents(document_type);
```

### Integration Points:
- **Medical Records Service:** Store extracted data in EHR
- **Billing Service:** Extract codes for billing
- **Laboratory Service:** Parse lab reports

---

## 🎯 Phase 3 Success Metrics

### Technical Metrics:
- ✅ All 9 services deployed and healthy
- ✅ Service discovery working (Eureka)
- ✅ API response times <200ms p95
- ✅ Database migrations successful
- ✅ Temporal workflows running reliably

### Functional Metrics:
- ✅ 5 care pathways operational
- ✅ Risk scoring accuracy >75%
- ✅ Triage accuracy >85%
- ✅ Provider search latency <200ms
- ✅ Document AI extraction accuracy >90%
- ✅ Claims workflow completion >95%

### Integration Metrics:
- ✅ All services communicating via Kafka
- ✅ All services registered with Eureka
- ✅ All services using PostgreSQL (no H2)
- ✅ All services publishing domain events

---

## 📁 Deliverables

### Services (9):
1. risk-scoring-service (Spring Boot + PostgreSQL + ML integration)
2. care-pathway-service (Spring Boot + PostgreSQL)
3. triage-service (Spring Boot + PostgreSQL + NLP)
4. provider-service (Spring Boot + PostGIS for geospatial)
5. patient-engagement-service (Spring Boot + PostgreSQL)
6. pricing-service (Spring Boot + PostgreSQL)
7. fraud-detection-service (Spring Boot + PostgreSQL + ML)
8. claims-orchestrator-service (Spring Boot + Temporal)
9. document-ai-service (Spring Boot + Python microservice for OCR/NLP)

### Database Schemas:
- 9 new PostgreSQL databases
- ~20 new tables
- Indexes, constraints, and foreign keys

### Documentation:
- API documentation (OpenAPI/Swagger) for each service
- Database schema documentation
- Integration guide
- Temporal workflow documentation

---

## 🚀 Next Phase Preview

**Phase 4 (Weeks 18-24): 30 Illness Areas**

Will implement:
- Remaining 25 care pathway templates (Heart & Circulation: 6, Metabolic & Kidney: 5, Respiratory: 4, Mental Health & Neuro: 7, Cancer: 2, Musculoskeletal: 3, Digestive & Autoimmune: 2, Sexual Health: 1)
- Condition-specific frontend widgets (30 illness areas)
- 30+ illness-specific ML models
- Real-time AI features for each condition

---

**Document Version:** 1.0
**Last Updated:** 2026-01-08
**Status:** ✅ Ready for Implementation
**Dependencies:** Phase 1 Complete, Phase 2 Complete
**Estimated Duration:** 6 weeks (Weeks 12-18)
