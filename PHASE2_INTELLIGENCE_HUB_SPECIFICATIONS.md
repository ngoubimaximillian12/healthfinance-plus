# Phase 2 Implementation Specifications: Healthcare Intelligence Hub

## Overview

**Timeline:** Weeks 6-12
**Focus:** AI/ML Platform & Intelligent Decision Making
**Deliverables:** 4 production ML models, Decision Service API, Safety Guardrails, Human Review Queue

---

## 📋 Objectives

### Primary Goals:
1. Deploy complete ML infrastructure (MLflow, TensorFlow Serving, Model Registry)
2. Implement Healthcare Intelligence Hub service (Port 8090)
3. Train and deploy 4 priority AI models
4. Build clinical safety guardrails with drug interaction checking
5. Create human-in-the-loop review system
6. Add explainability layer (SHAP/LIME)

### Success Criteria:
- ✅ ML models serving predictions with <200ms latency
- ✅ Decision API operational with 99.5% uptime
- ✅ Safety guardrails blocking unsafe prescriptions
- ✅ Human review queue processing uncertain decisions
- ✅ All AI decisions explainable with factor breakdowns

---

## 🏗️ Architecture Components

### 1. ML Infrastructure Services

#### MLflow (Port 5000)
**Purpose:** Model registry, experiment tracking, model versioning

**Configuration:**
```yaml
Backend Store: PostgreSQL (healthfinance database)
Artifact Store: MinIO (S3-compatible)
Default Artifact Root: s3://mlflow/artifacts
Endpoint: http://localhost:5000
```

**Features:**
- Model versioning (v1, v2, v3, etc.)
- Experiment tracking with metrics
- Model promotion workflow (Staging → Production)
- Model lineage and metadata
- REST API for model management

**Models to Register:**
1. triage-severity-v2 (symptom → severity score)
2. readmission-risk-v3 (patient data → 30-day readmission probability)
3. medication-adherence-v2 (patient history → adherence probability)
4. fraud-detection-v3 (claim data → fraud score)

#### TensorFlow Serving (Ports 8501/8500)
**Purpose:** High-performance ML model inference

**Configuration:**
```yaml
REST API Port: 8501
gRPC API Port: 8500
Model Base Path: /models
Model Config: multi-model serving
GPU Support: Enabled (if available)
Batch Processing: Enabled for throughput
```

**Model Deployment Structure:**
```
/models/
  ├── triage-severity-v2/
  │   ├── 1/  (version 1)
  │   │   └── saved_model.pb
  │   └── 2/  (version 2)
  ├── readmission-risk-v3/
  │   └── 1/
  ├── medication-adherence-v2/
  │   └── 1/
  └── fraud-detection-v3/
      └── 1/
```

**Performance Requirements:**
- Inference latency: <100ms p95, <200ms p99
- Throughput: 1000+ requests/second
- Batch size: Auto-tuned
- Model warm-up: Enabled

#### MinIO (Ports 9000/9001)
**Purpose:** S3-compatible object storage for ML artifacts

**Configuration:**
```yaml
API Port: 9000
Console Port: 9001
Root User: admin
Root Password: admin123 (change in production)
Buckets:
  - mlflow (ML artifacts)
  - models (trained models)
  - datasets (training data)
  - explainability (SHAP values, plots)
```

**Storage Organization:**
```
mlflow/
  ├── artifacts/
  │   ├── experiment-1/
  │   ├── experiment-2/
  │   └── ...
  └── models/
      ├── triage-severity-v2/
      └── ...

datasets/
  ├── training/
  │   ├── triage-training-2026-01.parquet
  │   ├── readmission-training-2026-01.parquet
  │   └── ...
  └── validation/

explainability/
  ├── shap-values/
  └── feature-importance/
```

#### Temporal (Ports 7233-7235)
**Purpose:** Workflow orchestration for long-running processes

**Use Cases:**
- Insurance claim processing (multi-day workflows)
- Prior authorization workflows
- Care pathway execution (months-long)
- Model retraining pipelines

**Configuration:**
```yaml
Frontend Port: 7233
Metrics Port: 7234
Worker Port: 7235
Database: PostgreSQL
Namespace: healthfinance-workflows
Task Queues:
  - claims-processing
  - prior-authorization
  - model-training
  - care-pathways
```

**Key Workflows to Implement:**
1. **ClaimProcessingWorkflow**
   - Steps: Validate → Fraud Check → Coverage Calc → Submit → Await Response
   - Duration: 2-14 days
   - Human tasks: Fraud review, appeals

2. **PriorAuthorizationWorkflow**
   - Steps: Gather docs → AI review → Human review → Submit
   - Duration: 1-3 days
   - SLA: 72 hours

3. **ModelRetrainingWorkflow**
   - Steps: Extract data → Feature engineering → Train → Validate → Deploy
   - Duration: 2-8 hours
   - Triggers: Weekly schedule, performance degradation

#### Temporal UI (Port 8088)
**Purpose:** Workflow monitoring and debugging

**Features:**
- Workflow execution history
- Task queue metrics
- Workflow search and filtering
- Error debugging
- Manual workflow triggering

---

### 2. Intelligence Hub Service (Port 8090)

#### Service Overview
**Spring Boot Application:** intelligence-hub-service
**Port:** 8090
**Database:** healthfinance_intelligence_hub (PostgreSQL)
**Dependencies:** Drools, TensorFlow Java Client, Temporal SDK, Kafka, Redis

#### Module Structure:
```
intelligence-hub-service/
├── src/main/java/com/healthfinance/intelligencehub/
│   ├── decision/           # Decision service (hybrid rules + ML)
│   ├── safety/             # Clinical safety guardrails
│   ├── ml/                 # ML model integration
│   ├── review/             # Human-in-the-loop review queue
│   ├── explanation/        # Explainability (SHAP/LIME)
│   ├── config/             # Configuration classes
│   ├── controller/         # REST controllers
│   ├── model/              # Domain models & DTOs
│   ├── repository/         # JPA repositories
│   └── service/            # Business logic services
└── src/main/resources/
    ├── application.properties
    ├── drools/             # Drools rule files (.drl)
    │   ├── prescription-safety.drl
    │   ├── triage-rules.drl
    │   └── clinical-alerts.drl
    └── db/migration/       # Flyway migration scripts
```

---

## 🧠 Core Components

### 1. Decision Service

#### Purpose:
Hybrid decision-making combining deterministic rules (Drools) and probabilistic ML models.

#### API Endpoints:

**POST /api/decisions/prescriptions/safety-check**
```json
Request:
{
  "patientId": "uuid",
  "medication": "Metformin",
  "dosage": "500mg BID",
  "duration": "90 days",
  "existingMedications": ["Warfarin 5mg"],
  "allergies": ["Penicillin"],
  "conditions": ["Type 2 Diabetes", "Atrial Fibrillation"]
}

Response:
{
  "decisionId": "dec-uuid-123",
  "safe": false,
  "riskScore": 0.85,
  "confidence": 0.92,
  "issues": [
    {
      "type": "DRUG_INTERACTION",
      "severity": "HIGH",
      "description": "Metformin + Warfarin may increase bleeding risk",
      "recommendation": "Monitor INR closely, consider alternative",
      "evidenceSource": "RULE: prescription-safety.drl line 45"
    }
  ],
  "humanReviewRequired": true,
  "explanation": {
    "model": "prescription-safety-v2",
    "factors": [
      {"factor": "Current medications", "impact": 0.60},
      {"factor": "Patient age (68)", "impact": 0.20},
      {"factor": "Renal function (eGFR 55)", "impact": 0.15},
      {"factor": "Diabetes duration", "impact": 0.05}
    ],
    "shapValues": {
      "url": "s3://explainability/shap/dec-uuid-123.png"
    }
  },
  "timestamp": "2026-01-08T14:30:00Z"
}
```

**POST /api/decisions/triage/severity**
```json
Request:
{
  "patientId": "uuid",
  "symptoms": [
    {"symptom": "Chest pain", "severity": 8, "duration": "30 minutes"},
    {"symptom": "Shortness of breath", "severity": 7, "duration": "30 minutes"},
    {"symptom": "Nausea", "severity": 5, "duration": "1 hour"}
  ],
  "vitals": {
    "heartRate": 110,
    "bloodPressure": "145/95",
    "temperature": 37.2,
    "oxygenSaturation": 94
  },
  "demographics": {
    "age": 65,
    "gender": "M"
  },
  "medicalHistory": ["Hypertension", "High Cholesterol"]
}

Response:
{
  "decisionId": "dec-uuid-456",
  "severity": "EMERGENCY",
  "urgencyScore": 0.95,
  "recommendedAction": "CALL_AMBULANCE",
  "reasoning": "RED FLAGS: Chest pain + shortness of breath + cardiac risk factors suggest possible MI",
  "triageLevel": "RESUS",
  "estimatedWaitTime": "0 minutes",
  "specialtyRecommendation": "Emergency Medicine / Cardiology",
  "redFlags": [
    "Chest pain lasting >20 minutes",
    "Associated shortness of breath",
    "Age >60 with cardiac risk factors"
  ],
  "explanation": {
    "model": "triage-severity-v2",
    "confidence": 0.95,
    "factors": [
      {"factor": "Chest pain severity", "impact": 0.50},
      {"factor": "Age + cardiac history", "impact": 0.30},
      {"factor": "Vital signs (HR, SpO2)", "impact": 0.15},
      {"factor": "Symptom combination", "impact": 0.05}
    ]
  },
  "timestamp": "2026-01-08T14:35:00Z"
}
```

**POST /api/decisions/risk/calculate**
```json
Request:
{
  "patientId": "uuid",
  "riskType": "30_DAY_READMISSION",
  "context": {
    "diagnosis": "Heart Failure Exacerbation",
    "lengthOfStay": 5,
    "priorAdmissions": 2,
    "comorbidities": ["Diabetes", "CKD Stage 3", "COPD"],
    "socialFactors": {
      "livesSinglehandedly": true,
      "transportAccess": false
    },
    "labResults": {
      "BNP": 850,
      "creatinine": 1.8,
      "sodium": 132
    }
  }
}

Response:
{
  "decisionId": "dec-uuid-789",
  "riskType": "30_DAY_READMISSION",
  "riskScore": 0.68,
  "riskCategory": "HIGH",
  "interventionsRecommended": [
    {
      "intervention": "Home health nurse visits 2x/week",
      "impactEstimate": "-15% risk reduction"
    },
    {
      "intervention": "Daily weight monitoring with alerts",
      "impactEstimate": "-10% risk reduction"
    },
    {
      "intervention": "Medication reconciliation + adherence support",
      "impactEstimate": "-8% risk reduction"
    },
    {
      "intervention": "Social work consult for transportation",
      "impactEstimate": "-5% risk reduction"
    }
  ],
  "explanation": {
    "model": "readmission-risk-v3",
    "confidence": 0.89,
    "factors": [
      {"factor": "Prior readmissions (2)", "impact": 0.25},
      {"factor": "Lives alone", "impact": 0.20},
      {"factor": "Multiple comorbidities (3)", "impact": 0.18},
      {"factor": "Elevated BNP", "impact": 0.15},
      {"factor": "Low sodium (132)", "impact": 0.12},
      {"factor": "No transport access", "impact": 0.10}
    ]
  },
  "carePathwayTriggered": "heart-failure-post-discharge",
  "timestamp": "2026-01-08T14:40:00Z"
}
```

**GET /api/decisions/{decisionId}**
- Retrieve decision details by ID

**GET /api/decisions/{decisionId}/explanation**
- Get detailed explainability report with SHAP plots

#### Decision Flow Logic:
```
1. Request received
2. Validate input
3. Load patient context (EHR data, history)
4. Run Drools rules engine (deterministic checks)
   ├─ Safety rules (drug interactions, contraindications)
   ├─ Clinical protocols (guidelines)
   └─ Policy rules (authorization, formulary)
5. If rules BLOCK → Return decision with reasoning
6. If rules ALLOW or UNCERTAIN → Call ML model
7. ML model inference (via TensorFlow Serving)
8. Combine rule output + ML prediction
9. Generate explanation (SHAP)
10. Determine if human review needed
    ├─ Low confidence (<0.8)
    ├─ High risk score (>0.7)
    └─ Rule-ML conflict
11. Save decision to audit log
12. Publish DecisionMadeEvent to Kafka
13. If human review needed → Create ReviewTask
14. Return decision response
```

#### Database Schema:

**Table: decisions**
```sql
CREATE TABLE decisions (
    decision_id UUID PRIMARY KEY,
    decision_type VARCHAR(100) NOT NULL,
    patient_id UUID NOT NULL,
    request_payload JSONB NOT NULL,
    response_payload JSONB NOT NULL,
    risk_score DECIMAL(5,4),
    confidence DECIMAL(5,4),
    human_review_required BOOLEAN DEFAULT false,
    review_task_id UUID,
    model_version VARCHAR(50),
    rule_version VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    created_by UUID,
    CONSTRAINT fk_review_task FOREIGN KEY (review_task_id) REFERENCES review_tasks(task_id)
);

CREATE INDEX idx_decisions_patient ON decisions(patient_id);
CREATE INDEX idx_decisions_type ON decisions(decision_type);
CREATE INDEX idx_decisions_review_required ON decisions(human_review_required);
CREATE INDEX idx_decisions_created_at ON decisions(created_at);
```

**Table: decision_factors**
```sql
CREATE TABLE decision_factors (
    factor_id UUID PRIMARY KEY,
    decision_id UUID NOT NULL,
    factor_name VARCHAR(255) NOT NULL,
    factor_value TEXT,
    impact_score DECIMAL(5,4),
    factor_type VARCHAR(50), -- RULE, ML_FEATURE, CLINICAL_FACT
    CONSTRAINT fk_decision FOREIGN KEY (decision_id) REFERENCES decisions(decision_id) ON DELETE CASCADE
);

CREATE INDEX idx_factors_decision ON decision_factors(decision_id);
```

---

### 2. Clinical Safety Guardrails

#### Purpose:
Prevent unsafe clinical actions through rules and ML-based checks.

#### Components:

**A. Drug Interaction Checker**

**Database: Drug Interactions**
```sql
CREATE TABLE drug_interactions (
    interaction_id UUID PRIMARY KEY,
    drug_a VARCHAR(255) NOT NULL,
    drug_b VARCHAR(255) NOT NULL,
    severity VARCHAR(50) NOT NULL, -- MILD, MODERATE, SEVERE, CONTRAINDICATED
    mechanism TEXT,
    clinical_effect TEXT,
    recommendation TEXT,
    evidence_level VARCHAR(50), -- HIGH, MODERATE, LOW
    source VARCHAR(255), -- FDA, DrugBank, etc.
    last_updated TIMESTAMP NOT NULL
);

CREATE INDEX idx_interactions_drug_a ON drug_interactions(drug_a);
CREATE INDEX idx_interactions_drug_b ON drug_interactions(drug_b);
CREATE INDEX idx_interactions_severity ON drug_interactions(severity);
```

**Data Source:**
- Initial load: DrugBank drug-drug interactions dataset (100,000+ interactions)
- Ongoing updates: FDA drug interaction tables

**Interaction Checking Logic:**
```
1. Get patient's active medications
2. Add proposed new medication
3. For each medication pair (N*(N-1)/2 combinations):
   ├─ Query drug_interactions table
   ├─ Check severity level
   └─ If SEVERE or CONTRAINDICATED → BLOCK
4. Apply patient-specific factors:
   ├─ Renal function (adjust for kidney-cleared drugs)
   ├─ Liver function (adjust for hepatically metabolized drugs)
   ├─ Age (geriatric/pediatric dosing)
   └─ Pregnancy status
5. Return interaction warnings ranked by severity
```

**B. Allergy Cross-Reactivity Checker**

**Database: Allergen Cross-Reactivity**
```sql
CREATE TABLE allergen_cross_reactivity (
    cross_reactivity_id UUID PRIMARY KEY,
    allergen VARCHAR(255) NOT NULL,
    cross_reactive_drug VARCHAR(255) NOT NULL,
    probability DECIMAL(3,2), -- 0.00 to 1.00
    severity VARCHAR(50),
    recommendation TEXT
);

-- Example: Penicillin allergy → avoid cephalosporins (10% cross-reactivity)
```

**C. Contraindication Checker (Drools Rules)**

**Rule File: prescription-safety.drl**
```drools
rule "Metformin contraindicated in severe CKD"
when
    $prescription : Prescription(medication == "Metformin")
    $patient : Patient(eGFR < 30)
then
    $prescription.addIssue(new SafetyIssue(
        "CONTRAINDICATION",
        "SEVERE",
        "Metformin contraindicated with eGFR <30 (lactic acidosis risk)",
        "Use insulin or other agent"
    ));
    $prescription.setBlocked(true);
end

rule "Beta-blocker caution in asthma"
when
    $prescription : Prescription(medicationClass == "Beta-blocker")
    $patient : Patient(hasCondition("Asthma") || hasCondition("COPD"))
then
    $prescription.addIssue(new SafetyIssue(
        "CAUTION",
        "MODERATE",
        "Beta-blockers may worsen bronchospasm in asthma/COPD",
        "Use cardioselective beta-blocker with caution, monitor closely"
    ));
end

rule "ACE inhibitor + pregnancy"
when
    $prescription : Prescription(medicationClass == "ACE Inhibitor")
    $patient : Patient(isPregnant == true)
then
    $prescription.addIssue(new SafetyIssue(
        "CONTRAINDICATION",
        "SEVERE",
        "ACE inhibitors are teratogenic (fetal kidney damage)",
        "Stop immediately, use alternative (methyldopa, labetalol)"
    ));
    $prescription.setBlocked(true);
end

rule "NSAIDs + anticoagulants bleeding risk"
when
    $prescription : Prescription(medicationClass == "NSAID")
    $patient : Patient(takingMedication("Warfarin") || takingMedication("Apixaban") || takingMedication("Rivaroxaban"))
then
    $prescription.addIssue(new SafetyIssue(
        "INTERACTION",
        "HIGH",
        "NSAIDs + anticoagulants increase bleeding risk",
        "Avoid combination or use gastroprotection (PPI), monitor closely"
    ));
end
```

**D. Dosing Safety Checks**

**Renal Dose Adjustment:**
```
1. Get patient's eGFR (estimated glomerular filtration rate)
2. Check if medication requires renal dose adjustment
3. Apply dosing guidelines:
   ├─ eGFR >60: No adjustment
   ├─ eGFR 30-60: Reduce dose by 25-50%
   ├─ eGFR 15-30: Reduce dose by 50-75%
   └─ eGFR <15: Contraindicated or minimal dose
4. Warn if prescribed dose exceeds recommendation
```

**Hepatic Dose Adjustment:**
```
Similar logic using Child-Pugh score (A, B, C)
```

**Geriatric Dosing (Beers Criteria):**
```sql
CREATE TABLE beers_criteria (
    criteria_id UUID PRIMARY KEY,
    medication VARCHAR(255) NOT NULL,
    concern TEXT,
    recommendation TEXT,
    strength_of_recommendation VARCHAR(50) -- STRONG, MODERATE, WEAK
);

-- Example: "Avoid benzodiazepines in adults >65 (fall risk, cognitive impairment)"
```

---

### 3. Human-in-the-Loop Review System

#### Purpose:
Queue uncertain or high-risk AI decisions for human expert review.

#### Components:

**A. Review Task Queue**

**Database Schema:**
```sql
CREATE TABLE review_tasks (
    task_id UUID PRIMARY KEY,
    decision_id UUID NOT NULL,
    task_type VARCHAR(100) NOT NULL, -- PRESCRIPTION_SAFETY, FRAUD_ALERT, TRIAGE_UNCERTAIN, DIAGNOSIS_CONFIRMATION
    priority VARCHAR(50) NOT NULL, -- URGENT, HIGH, MEDIUM, LOW
    status VARCHAR(50) NOT NULL, -- PENDING, CLAIMED, IN_REVIEW, COMPLETED, ESCALATED
    assigned_to UUID, -- User ID of reviewer
    patient_id UUID NOT NULL,
    context JSONB NOT NULL, -- All relevant data for review
    ai_recommendation JSONB NOT NULL,
    ai_confidence DECIMAL(5,4),
    due_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    claimed_at TIMESTAMP,
    completed_at TIMESTAMP,
    review_decision JSONB,
    reviewer_notes TEXT,
    CONSTRAINT fk_decision FOREIGN KEY (decision_id) REFERENCES decisions(decision_id),
    CONSTRAINT fk_assigned_user FOREIGN KEY (assigned_to) REFERENCES users(user_id)
);

CREATE INDEX idx_tasks_status ON review_tasks(status);
CREATE INDEX idx_tasks_priority ON review_tasks(priority);
CREATE INDEX idx_tasks_assigned_to ON review_tasks(assigned_to);
CREATE INDEX idx_tasks_due_date ON review_tasks(due_date);
CREATE INDEX idx_tasks_patient ON review_tasks(patient_id);
```

**B. Review Queue API**

**GET /api/review-queue/my-tasks**
```json
Query Params:
- status (PENDING, IN_REVIEW, COMPLETED)
- priority (URGENT, HIGH, MEDIUM, LOW)
- taskType
- page, size, sort

Response:
{
  "tasks": [
    {
      "taskId": "task-uuid-123",
      "taskType": "PRESCRIPTION_SAFETY",
      "priority": "URGENT",
      "status": "PENDING",
      "patientName": "John Doe",
      "patientId": "patient-uuid",
      "aiRecommendation": "Block prescription due to interaction",
      "aiConfidence": 0.75,
      "dueDate": "2026-01-08T18:00:00Z",
      "createdAt": "2026-01-08T14:30:00Z",
      "summary": "Metformin + Warfarin interaction - needs review"
    },
    ...
  ],
  "totalElements": 25,
  "totalPages": 3,
  "currentPage": 0
}
```

**GET /api/review-queue/tasks/{taskId}**
```json
Response:
{
  "taskId": "task-uuid-123",
  "decisionId": "dec-uuid-456",
  "taskType": "PRESCRIPTION_SAFETY",
  "priority": "URGENT",
  "status": "PENDING",
  "patient": {
    "patientId": "patient-uuid",
    "name": "John Doe",
    "age": 68,
    "mrn": "MRN-123456",
    "conditions": ["Type 2 Diabetes", "Atrial Fibrillation", "Hypertension"],
    "allergies": ["Penicillin"]
  },
  "context": {
    "proposedMedication": {
      "name": "Metformin",
      "dosage": "500mg BID",
      "duration": "90 days"
    },
    "currentMedications": [
      {"name": "Warfarin", "dosage": "5mg daily", "startDate": "2024-06-15"},
      {"name": "Lisinopril", "dosage": "20mg daily", "startDate": "2023-02-10"}
    ],
    "recentLabs": {
      "eGFR": 55,
      "INR": 2.3,
      "HbA1c": 8.2
    }
  },
  "aiRecommendation": {
    "action": "BLOCK",
    "reasoning": "Moderate drug interaction detected between Metformin and Warfarin. Increased bleeding risk. Patient has borderline renal function (eGFR 55).",
    "confidence": 0.75,
    "alternatives": [
      "Consider insulin instead",
      "Use alternative oral agent (e.g., SGLT2 inhibitor)",
      "If Metformin necessary, monitor INR weekly for 1 month"
    ]
  },
  "aiExplanation": {
    "factors": [
      {"factor": "Warfarin interaction", "impact": 0.50},
      {"factor": "Borderline renal function", "impact": 0.30},
      {"factor": "Patient age (68)", "impact": 0.15},
      {"factor": "Polypharmacy", "impact": 0.05}
    ]
  },
  "dueDate": "2026-01-08T18:00:00Z",
  "createdAt": "2026-01-08T14:30:00Z"
}
```

**POST /api/review-queue/tasks/{taskId}/claim**
```json
Request: (empty body, user from JWT)

Response:
{
  "taskId": "task-uuid-123",
  "status": "CLAIMED",
  "assignedTo": "reviewer-uuid",
  "claimedAt": "2026-01-08T15:00:00Z"
}
```

**POST /api/review-queue/tasks/{taskId}/complete**
```json
Request:
{
  "decision": "APPROVE_WITH_MODIFICATIONS",
  "modifications": {
    "dosage": "250mg BID (reduced dose)",
    "additionalInstructions": "Monitor INR weekly for 4 weeks",
    "followUp": "Recheck kidney function in 2 weeks"
  },
  "reasoning": "Metformin is appropriate for glycemic control. Reduced dose safe given eGFR 55. Close monitoring mitigates interaction risk.",
  "overrideAI": true,
  "notes": "Discussed with patient. Patient understands bleeding risk and will report any unusual bruising immediately."
}

Response:
{
  "taskId": "task-uuid-123",
  "status": "COMPLETED",
  "completedAt": "2026-01-08T15:30:00Z",
  "reviewDecision": {
    "decision": "APPROVE_WITH_MODIFICATIONS",
    "modifications": {...},
    "reasoning": "...",
    "overrideAI": true
  },
  "feedbackToModel": {
    "correct": false,
    "actualOutcome": "APPROVE_WITH_MODIFICATIONS",
    "usedForRetraining": true
  }
}
```

**GET /api/review-queue/metrics**
```json
Response:
{
  "queueMetrics": {
    "totalPending": 15,
    "totalInReview": 8,
    "totalCompletedToday": 42,
    "avgReviewTime": "12 minutes",
    "slaCompliance": 0.94, // 94% completed within SLA
    "byPriority": {
      "URGENT": 3,
      "HIGH": 7,
      "MEDIUM": 4,
      "LOW": 1
    }
  },
  "reviewerMetrics": {
    "tasksCompletedToday": 8,
    "avgReviewTime": "10 minutes",
    "aiAgreementRate": 0.75, // 75% agree with AI
    "overrideRate": 0.25
  },
  "modelPerformance": {
    "precisionAtConfidence0.8": 0.92,
    "recallAtConfidence0.8": 0.88,
    "humanReviewRate": 0.15, // 15% of decisions need review
    "falsePositiveRate": 0.08
  }
}
```

**C. Review Assignment Logic**

**Priority Calculation:**
```
Priority = f(patient_risk, urgency, confidence)

URGENT:
- Patient in hospital
- Time-sensitive decision (surgery, emergency meds)
- High patient risk + low AI confidence

HIGH:
- Moderate patient risk
- Due within 24 hours
- Prescription with serious interaction

MEDIUM:
- Low patient risk
- Due within 72 hours
- Standard uncertain decisions

LOW:
- Routine review
- Due within 1 week
- Educational cases
```

**Auto-Assignment:**
```
1. New task created
2. Check reviewer availability
3. Match by specialty:
   ├─ Prescription safety → Pharmacist or Physician
   ├─ Triage → Nurse or Physician
   ├─ Fraud → Claims analyst
   └─ Diagnosis → Specialist physician
4. Balance workload (round-robin within specialty)
5. Assign task
6. Notify reviewer (email, push notification)
```

**Escalation:**
```
If task not claimed within:
- URGENT: 30 minutes → Escalate to senior reviewer
- HIGH: 2 hours → Escalate
- MEDIUM: 8 hours → Escalate
- LOW: 24 hours → Escalate
```

---

### 4. Model Explainability Service

#### Purpose:
Provide human-interpretable explanations for ML model predictions.

#### Techniques:

**A. SHAP (SHapley Additive exPlanations)**

**Use Case:** Feature importance for individual predictions

**Implementation:**
- Python service (Flask/FastAPI) separate from Java services
- Exposes REST API: POST /explain/shap
- Loads saved models from MLflow
- Generates SHAP values on-demand or pre-computed

**SHAP Explanation Flow:**
```
1. Decision made by ML model
2. Extract input features used for prediction
3. Call SHAP service with model ID + features
4. SHAP service:
   ├─ Loads model from MLflow
   ├─ Computes SHAP values (TreeExplainer for tree models, KernelExplainer for others)
   ├─ Generates waterfall plot (feature contributions)
   ├─ Generates force plot (push toward/away from prediction)
   ├─ Saves plots to MinIO (s3://explainability/...)
   └─ Returns JSON with SHAP values + plot URLs
5. Include SHAP explanation in decision response
```

**SHAP Output Example:**
```json
{
  "baseValue": 0.32, // Average prediction across training set
  "prediction": 0.68, // Actual prediction for this patient
  "shapValues": [
    {"feature": "Prior readmissions (2)", "shapValue": 0.18},
    {"feature": "Lives alone", "shapValue": 0.12},
    {"feature": "CKD Stage 3", "shapValue": 0.08},
    {"feature": "Age (72)", "shapValue": 0.05},
    {"feature": "Length of stay (5 days)", "shapValue": -0.02},
    {"feature": "Medication adherence (85%)", "shapValue": -0.05}
  ],
  "waterfallPlotUrl": "s3://explainability/shap/dec-uuid-123-waterfall.png",
  "forcePlotUrl": "s3://explainability/shap/dec-uuid-123-force.png"
}
```

**B. LIME (Local Interpretable Model-agnostic Explanations)**

**Use Case:** Explain complex black-box models

**Implementation:**
- Used for deep learning models (if deployed)
- Perturbs input features and observes prediction changes
- Fits local linear model around the prediction

**C. Feature Importance (Global)**

**Use Case:** Understand overall model behavior

**Compute Periodically:**
```
1. Weekly batch job
2. Compute feature importance across validation set
3. Generate global SHAP summary plots
4. Track feature importance drift over time
5. Alert if top features change significantly
```

**Feature Importance Dashboard:**
```
Top 10 Features for Readmission Risk Model:
1. Number of prior readmissions (importance: 0.25)
2. Length of hospital stay (importance: 0.18)
3. Number of comorbidities (importance: 0.15)
4. Lives alone (importance: 0.12)
5. Medication count (importance: 0.10)
6. Age (importance: 0.08)
7. Emergency admission (importance: 0.06)
8. Elevated BNP (importance: 0.04)
9. Low sodium (importance: 0.02)
10. Discharge destination (importance: 0.01)
```

---

## 🤖 ML Models to Train & Deploy

### Model 1: Triage Severity Classifier (triage-severity-v2)

**Purpose:** Predict severity/urgency from patient-reported symptoms

**Model Type:** Multi-class classification (XGBoost or LightGBM)

**Input Features:**
- Patient demographics (age, gender)
- Symptoms (encoded as binary features or embeddings from NLP)
- Symptom severity scores (1-10)
- Symptom duration
- Vital signs (if available)
- Medical history flags (chronic conditions)

**Output Classes:**
- EMERGENCY (call ambulance)
- URGENT (ED within 2 hours)
- SOON (primary care within 24 hours)
- ROUTINE (primary care within 1 week)
- SELF_CARE (manage at home)

**Target Performance:**
- Accuracy: >85%
- Recall for EMERGENCY class: >95% (cannot miss emergencies)
- Precision for EMERGENCY class: >70% (minimize false alarms)
- Inference latency: <100ms p99

**Training Data Requirements:**
- Historical triage records (100,000+ cases)
- Ground truth: Final triage decision by nurse/physician
- Balanced classes (oversample EMERGENCY if needed)

**Deployment:**
- TensorFlow Serving
- Model version: v2
- Endpoint: `/v1/models/triage-severity-v2:predict`

---

### Model 2: 30-Day Readmission Risk (readmission-risk-v3)

**Purpose:** Predict probability of hospital readmission within 30 days of discharge

**Model Type:** Binary classification (Gradient Boosting or Neural Network)

**Input Features:**
- Patient demographics (age, gender, race)
- Primary diagnosis (ICD-10 codes)
- Comorbidities (Elixhauser or Charlson index)
- Length of stay
- Number of prior admissions (1 year, 6 months)
- Admission type (elective vs emergency)
- Discharge destination (home, SNF, rehab)
- Lab values at discharge (BNP, creatinine, sodium, hemoglobin)
- Medication count
- Social factors (lives alone, transportation access)

**Output:**
- Probability score (0.00 to 1.00)
- Risk category (LOW <0.3, MODERATE 0.3-0.6, HIGH >0.6)

**Target Performance:**
- AUC-ROC: >0.75
- Recall at HIGH risk threshold: >80%
- Precision at HIGH risk threshold: >50%
- Inference latency: <150ms p99

**Training Data Requirements:**
- Historical admission/discharge records (50,000+ admissions)
- Ground truth: Readmission within 30 days (yes/no)
- Handle class imbalance (readmissions ~15-20% baseline rate)

**Deployment:**
- TensorFlow Serving
- Model version: v3
- Endpoint: `/v1/models/readmission-risk-v3:predict`

**Use Cases:**
- Trigger high-risk care pathways
- Allocate care coordination resources
- Justify home health services

---

### Model 3: Medication Adherence Predictor (medication-adherence-v2)

**Purpose:** Predict whether patient will adhere to prescribed medication regimen

**Model Type:** Binary classification (Random Forest or XGBoost)

**Input Features:**
- Patient demographics (age, gender, socioeconomic status proxies)
- Medication complexity (number of medications, dosing frequency)
- Prior adherence history (refill rates)
- Condition severity
- Side effect history
- Cost/insurance coverage
- Pharmacy access
- Health literacy indicators

**Output:**
- Adherence probability (0.00 to 1.00)
- Category (NON_ADHERENT <0.6, MODERATE 0.6-0.8, ADHERENT >0.8)

**Target Performance:**
- AUC-ROC: >0.70
- Recall for NON_ADHERENT: >75%
- Inference latency: <100ms p99

**Training Data Requirements:**
- Prescription fill/refill records (200,000+ prescriptions)
- Ground truth: Proportion of days covered (PDC) >80% = adherent
- Link prescriptions to patient outcomes

**Deployment:**
- TensorFlow Serving
- Model version: v2
- Endpoint: `/v1/models/medication-adherence-v2:predict`

**Use Cases:**
- Target adherence interventions (reminders, counseling)
- Predict medication waste
- Adjust care plans for expected non-adherence

---

### Model 4: Fraud Detection (fraud-detection-v3)

**Purpose:** Detect fraudulent insurance claims

**Model Type:** Anomaly detection + Supervised classification (Isolation Forest + XGBoost ensemble)

**Input Features:**
- Claim amount
- Diagnosis codes (ICD-10)
- Procedure codes (CPT)
- Provider ID
- Provider specialty
- Patient demographics
- Service date vs submission date lag
- Claim frequency patterns
- Billing code combinations (unusual bundles)
- Provider claim volume
- Geographic location

**Output:**
- Fraud probability (0.00 to 1.00)
- Risk category (LOW, MODERATE, HIGH, CRITICAL)
- Anomaly flags (unusual combinations, outlier amounts)

**Target Performance:**
- AUC-ROC: >0.85
- Precision at HIGH threshold: >60% (reduce false positives)
- Recall at HIGH threshold: >70%
- Inference latency: <300ms p99

**Training Data Requirements:**
- Historical claims (1,000,000+ claims)
- Ground truth: Confirmed fraud cases (investigations, audits)
- Highly imbalanced (fraud ~1-2% of claims)
- Use synthetic data augmentation (SMOTE)

**Deployment:**
- TensorFlow Serving
- Model version: v3
- Endpoint: `/v1/models/fraud-detection-v3:predict`

**Use Cases:**
- Flag claims for review before payment
- Identify fraud patterns across providers
- Reduce improper payments

---

## 📊 Model Training Pipeline

### Infrastructure:

**Training Environment:**
- GPU-enabled instance (AWS p3.2xlarge or equivalent)
- Jupyter notebooks for experimentation
- MLflow for experiment tracking
- Feast for feature store (optional, Phase 6)

### Pipeline Steps:

**1. Data Extraction**
```
Source: PostgreSQL databases (medical_records, prescriptions, claims, etc.)
Tool: Debezium CDC → Kafka → Data Lake (Phase 6) OR direct SQL queries (Phase 2)
Output: Parquet files in MinIO (s3://datasets/training/)
```

**2. Feature Engineering**
```
Tool: Python (pandas, scikit-learn)
Steps:
- Aggregate patient history
- Encode categorical variables
- Create derived features (e.g., comorbidity indices)
- Handle missing values (imputation)
- Feature scaling (standardization/normalization)
Output: Feature matrix (X) + labels (y)
```

**3. Model Training**
```
Tool: Python (scikit-learn, XGBoost, TensorFlow/Keras)
Steps:
- Train/validation/test split (70/15/15)
- Cross-validation (5-fold)
- Hyperparameter tuning (GridSearchCV or Bayesian optimization)
- Train final model
- Evaluate on test set
Output: Trained model (.pkl or SavedModel format)
```

**4. Model Evaluation**
```
Metrics:
- Classification: Accuracy, Precision, Recall, F1, AUC-ROC
- Regression: MAE, RMSE, R²
- Calibration: Calibration curve, Brier score
- Fairness: Disparate impact, equalized odds (check for bias across demographics)

Validation:
- Clinical validation (review predictions with clinicians)
- Explainability check (SHAP values make sense)
- Edge case testing (extreme values, rare conditions)
```

**5. Model Registration (MLflow)**
```
Steps:
- Log experiment (parameters, metrics, artifacts)
- Register model in MLflow Model Registry
- Set model stage: Staging
- Tag with metadata (dataset version, training date, evaluation metrics)
```

**6. Model Deployment**
```
Steps:
- Export model to TensorFlow SavedModel format
- Copy to /models/{model-name}/{version}/ directory
- TensorFlow Serving auto-detects new version
- Warm up model (send test requests)
- Update intelligence-hub-service to use new version
- Canary deployment (10% traffic → 50% → 100%)
- Monitor performance (latency, accuracy)
- Promote to Production stage in MLflow if successful
```

---

## 🧪 Testing Strategy

### Unit Tests:
- Decision service logic
- Drools rules (assert expected outcomes)
- Safety checker logic
- Review queue assignment

### Integration Tests:
- Intelligence hub → TensorFlow Serving
- Intelligence hub → MLflow
- Intelligence hub → PostgreSQL
- Intelligence hub → Kafka
- End-to-end decision flow

### Performance Tests:
- Load test decision API (1000 req/s)
- Measure inference latency (p50, p95, p99)
- Database query optimization

### Clinical Validation:
- Review 100 sample decisions with clinicians
- Validate triage recommendations
- Validate safety alerts (false positive rate)
- Validate explainability (do explanations make sense?)

---

## 📈 Monitoring & Metrics

### Model Performance Monitoring:

**Metrics to Track:**
- Prediction distribution (drift detection)
- Feature distribution (data drift)
- Accuracy over time
- Confidence calibration
- Human override rate

**Alerting:**
- Accuracy drops below threshold
- Prediction distribution shift >10%
- Inference latency >300ms p99
- Model errors (exceptions during inference)

### Decision API Monitoring:

**Metrics:**
- Request rate (requests/second)
- Latency (p50, p95, p99)
- Error rate
- Human review rate
- Safety alert rate

**Dashboards:**
- Grafana dashboard with:
  - Request throughput
  - Latency trends
  - Model performance
  - Review queue depth
  - Top safety issues

---

## 🔄 Feedback Loop

### Continuous Learning:

**Human Feedback Collection:**
```
1. Reviewer completes review task
2. Capture: AI prediction vs Human decision
3. Store in feedback database:
   - decision_id
   - ai_prediction
   - human_decision
   - agreement (yes/no)
   - feedback_notes
4. Weekly: Analyze disagreements
5. Monthly: Retrain models with new data + feedback
```

**Model Retraining Triggers:**
- Scheduled: Monthly
- Performance degradation: Accuracy <threshold
- Data drift detected
- New clinical guidelines released

---

## 🎯 Success Metrics (Phase 2 Exit Criteria)

### Technical Metrics:
- ✅ 4 ML models deployed and serving predictions
- ✅ Decision API: 99.5% uptime, <200ms latency p99
- ✅ Safety guardrails: 100% recall for severe contraindications
- ✅ Review queue: <4 hour median time to review
- ✅ Explainability: 100% of decisions have SHAP explanations

### Clinical Metrics:
- ✅ Clinician acceptance rate: >80% agree with AI recommendations
- ✅ False positive rate (safety alerts): <15%
- ✅ Triage accuracy: >85% match nurse triage
- ✅ Readmission risk AUC: >0.75

### Operational Metrics:
- ✅ Human review rate: <20% (80% of decisions auto-approved)
- ✅ Average review time: <15 minutes per task
- ✅ Model inference cost: <$0.01 per prediction

---

## 📁 Deliverables

### Code:
- intelligence-hub-service (Spring Boot application)
- SHAP explainability service (Python Flask/FastAPI)
- Drools rule files (prescription-safety.drl, triage-rules.drl)
- Model training notebooks (Jupyter)

### Models:
- triage-severity-v2 (TensorFlow SavedModel)
- readmission-risk-v3 (TensorFlow SavedModel)
- medication-adherence-v2 (TensorFlow SavedModel)
- fraud-detection-v3 (TensorFlow SavedModel)

### Infrastructure:
- docker-compose.yml (with MLflow, TensorFlow Serving, Temporal)
- PostgreSQL schemas (decisions, review_tasks, drug_interactions)
- Kafka topics (decision-events, review-tasks-events)

### Documentation:
- API documentation (OpenAPI/Swagger)
- Model cards (model description, performance, limitations)
- Clinical decision support rules documentation
- Explainability guide for clinicians

---

## 🚀 Next Phase Preview

**Phase 3 (Weeks 12-18): Core New Services**

Will implement 9 new microservices:
1. risk-scoring-service (real-time patient risk stratification)
2. care-pathway-service (5 illness pathways MVP)
3. triage-service (symptom checker + routing)
4. provider-service (provider directory)
5. patient-engagement-service (engagement tracking)
6. pricing-service (cost transparency)
7. fraud-detection-service (claims fraud)
8. claims-orchestrator-service (Temporal workflows)
9. document-ai-service (OCR + extraction)

These services will build on the Intelligence Hub foundation and extend AI capabilities across the platform.

---

**Document Version:** 1.0
**Last Updated:** 2026-01-08
**Status:** ✅ Ready for Implementation
**Dependencies:** Phase 1 Complete
**Estimated Duration:** 6 weeks (Weeks 6-12)
