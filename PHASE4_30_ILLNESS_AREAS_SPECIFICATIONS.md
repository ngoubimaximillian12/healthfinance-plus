# Phase 4 Implementation Specifications: 30 Illness Areas with Real-Time AI

## Overview

**Timeline:** Weeks 18-24
**Focus:** Disease-Specific Care Pathways, Condition-Specific ML Models, Real-Time Monitoring
**Deliverables:** 30 complete illness areas, 30+ ML models, condition-specific frontend widgets

---

## 📋 Objectives

### Primary Goals:
1. Implement all 30 illness-specific care pathways
2. Build condition-specific frontend widgets for each illness
3. Train and deploy 30+ illness-specific ML models
4. Create real-time monitoring and alerting for each condition
5. Build symptom tracking and patient-reported outcome tools
6. Implement condition-specific educational content delivery

### Success Criteria:
- ✅ All 30 illness areas fully operational
- ✅ Each illness has dedicated care pathway, ML model, and frontend widget
- ✅ Real-time alerts working for deterioration detection
- ✅ Patient adherence tracking active for all chronic conditions
- ✅ Clinician dashboards showing condition-specific patient cohorts
- ✅ 85%+ patient engagement with symptom check-ins

---

## 🏗️ Implementation Framework

### Standard Components per Illness Area:

1. **Care Pathway Template**
   - Multi-stage treatment plan
   - Automated task assignment
   - Milestone tracking
   - Duration: Varies by condition (3 months to 24 months)

2. **Risk Scoring Model**
   - Progression/complication prediction
   - Real-time risk calculation
   - Input: Condition-specific biomarkers, vitals, symptoms
   - Output: Risk score 0.00-1.00, category (LOW/MODERATE/HIGH)

3. **Symptom Monitoring**
   - Patient-reported outcomes (PROs)
   - Validated questionnaires (PHQ-9, ACT, WOMAC, etc.)
   - Frequency: Daily, weekly, or monthly based on condition

4. **Adherence Tracking**
   - Medication adherence
   - Lifestyle adherence (diet, exercise)
   - Treatment plan adherence

5. **Alert Triggers**
   - Real-time deterioration detection
   - Red flag symptoms
   - Lab value thresholds
   - Vital sign abnormalities

6. **Educational Content**
   - Condition basics
   - Self-management techniques
   - Lifestyle modifications
   - Medication information

7. **Frontend Components**
   - Patient dashboard widget
   - Clinician cohort view
   - Trend charts
   - Action items

---

## ❤️ Category 1: Heart & Circulation (6 Conditions)

### 1.1 Hypertension (High Blood Pressure)

**Prevalence:** ~47% of US adults
**Complexity:** Moderate
**Pathway Duration:** 6 months

#### Care Pathway Stages:

**Stage 1: Initial Assessment (Week 1)**
- Tasks:
  - 3 BP readings (different days/times)
  - Lab work: BMP, lipid panel, TSH, urinalysis
  - ECG if indicated
  - Lifestyle assessment
- Patient Education:
  - BP monitoring technique
  - DASH diet principles
  - Exercise recommendations
- Medication: Consider if BP >140/90

**Stage 2: Medication Titration (Weeks 2-8)**
- Tasks:
  - Weekly BP checks (home monitoring)
  - Medication adjustments every 2-4 weeks
  - Side effect monitoring
- Patient Education:
  - Medication adherence
  - Recognizing side effects
  - When to seek help
- Target: BP <130/80

**Stage 3: Stabilization (Months 3-6)**
- Tasks:
  - Biweekly BP monitoring
  - Monthly provider check-ins
  - Lab recheck at 3 months
- Patient Education:
  - Long-term adherence strategies
  - Lifestyle reinforcement
- Milestone: BP at goal for 3 consecutive months

#### AI Features:

**ML Model: bp-trend-predictor-v1**
- Input: Time-series BP readings, medication changes, lifestyle factors
- Output: Trajectory prediction (improving, stable, worsening)
- Alerts: BP >180/120 (hypertensive crisis), missed readings >7 days

**Real-Time Monitoring:**
- Daily BP input via mobile app
- Automatic alerts for out-of-range values
- Medication adherence reminders

**Frontend Widget: Hypertension Dashboard**
```
Components:
- BP Trend Chart (30-day, 90-day, 1-year views)
- Last Reading Card (with color coding: green/yellow/red)
- Medication List with adherence %
- Lifestyle Tracking (sodium intake, exercise minutes)
- Next Tasks (upcoming BP check, lab due, appointment)
- Educational Content Recommendations
```

**Clinician View:**
```
- Patient Cohort List (sorted by control status)
- Uncontrolled BP Alert Queue
- Medication Non-Adherence Alerts
- Patients Due for Lab Work
```

#### Database Schema Extensions:

```sql
CREATE TABLE hypertension_data (
    data_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    pathway_id UUID,
    systolic INTEGER NOT NULL,
    diastolic INTEGER NOT NULL,
    pulse INTEGER,
    measurement_time TIMESTAMP NOT NULL,
    measurement_location VARCHAR(50), -- HOME, CLINIC, AMBULATORY
    arm VARCHAR(10), -- LEFT, RIGHT
    position VARCHAR(20), -- SITTING, STANDING, LYING
    notes TEXT,
    CONSTRAINT chk_bp_range CHECK (systolic BETWEEN 70 AND 250 AND diastolic BETWEEN 40 AND 150)
);

CREATE INDEX idx_hypertension_patient ON hypertension_data(patient_id);
CREATE INDEX idx_hypertension_time ON hypertension_data(measurement_time);
```

#### Events Published:
- BPReadingRecordedEvent
- HypertensiveCrisisDetectedEvent
- BPControlAchievedEvent
- MedicationAdjustedEvent

---

### 1.2 Coronary Heart Disease / Angina

**Prevalence:** ~6.7% of US adults
**Complexity:** High
**Pathway Duration:** 12 months

#### Care Pathway Stages:

**Stage 1: Diagnosis & Risk Stratification (Weeks 1-2)**
- Tasks:
  - Stress test or cardiac imaging
  - Coronary angiography if indicated
  - Complete lipid panel, HbA1c, kidney function
  - Risk score calculation (HEART score, GRACE score)
- Patient Education:
  - Understanding angina
  - Recognizing warning signs
  - When to use nitroglycerin

**Stage 2: Medical Optimization (Months 1-3)**
- Tasks:
  - Dual antiplatelet therapy
  - Statin initiation/optimization
  - Beta-blocker, ACE inhibitor as indicated
  - Cardiac rehab referral
- Patient Education:
  - Medication adherence critical importance
  - Lifestyle modifications (smoking cessation, diet, exercise)
- Milestone: Symptom-free for 4 weeks

**Stage 3: Cardiac Rehabilitation (Months 3-6)**
- Tasks:
  - Supervised exercise 3x/week
  - Nutrition counseling
  - Stress management
- Patient Education:
  - Gradual activity progression
  - Safe exercise guidelines

**Stage 4: Long-Term Management (Months 6-12)**
- Tasks:
  - Quarterly follow-ups
  - Annual stress test
  - Lipid panel every 6 months
- Patient Education:
  - Lifelong medication adherence
  - Warning signs of MI

#### AI Features:

**ML Model: angina-risk-v1**
- Input: Frequency of angina episodes, exercise tolerance, biomarkers (troponin, BNP)
- Output: Risk of major adverse cardiovascular event (MACE) within 1 year
- Alerts: New onset chest pain, rest angina, increasing frequency

**Chest Pain Triage Model: chest-pain-classifier-v2**
- Input: Symptom description, duration, severity, associated symptoms
- Output: Probability (MI, unstable angina, stable angina, non-cardiac)
- Action: EMERGENCY if MI probability >0.6

**Frontend Widget: Cardiac Health Dashboard**
```
Components:
- Angina Episode Tracker (frequency, severity, triggers)
- Medication Adherence Chart
- Cardiac Rehab Progress (sessions completed, exercise capacity)
- Nitrate Usage Log
- Next Stress Test Due
- Warning Signs Checklist
```

#### Database Schema:

```sql
CREATE TABLE angina_episodes (
    episode_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    episode_time TIMESTAMP NOT NULL,
    severity INTEGER CHECK (severity BETWEEN 1 AND 10),
    duration_minutes INTEGER,
    trigger VARCHAR(255),
    relieved_by VARCHAR(100), -- REST, NITROGLYCERIN, NONE
    associated_symptoms TEXT[],
    classification VARCHAR(50), -- STABLE, UNSTABLE, VARIANT
    required_emergency_care BOOLEAN DEFAULT false
);

CREATE INDEX idx_angina_patient ON angina_episodes(patient_id);
CREATE INDEX idx_angina_time ON angina_episodes(episode_time);
```

---

### 1.3 Heart Failure

**Prevalence:** ~6.2 million US adults
**Complexity:** Very High
**Pathway Duration:** 12 months (intensive), then ongoing

#### Care Pathway Stages:

**Stage 1: Immediate Post-Discharge (Days 1-7)**
- Tasks:
  - Daily weight monitoring (alert if +2 lbs/day or +5 lbs/week)
  - Daily check-in calls (nurse or automated)
  - Medication reconciliation
  - Follow-up appointment within 7 days
  - Diuretic titration
- Patient Education:
  - Fluid restriction (1.5-2L/day)
  - Symptom recognition (SOB, edema, fatigue)
  - When to call provider

**Stage 2: Early Recovery (Weeks 2-4)**
- Tasks:
  - Home health nurse visits 2-3x/week
  - Telemonitoring (weight, BP, pulse ox)
  - GDMT optimization (ACE/ARB/ARNI, beta-blocker, MRA, SGLT2i)
  - BNP monitoring
- Patient Education:
  - Gradual activity increase
  - Low-sodium diet (<2g/day)
  - Fluid management

**Stage 3: Stabilization (Weeks 5-12)**
- Tasks:
  - Weekly weight monitoring
  - Biweekly provider check-ins
  - Cardiac rehab enrollment
  - Device evaluation (ICD, CRT) if indicated
- Milestone: No decompensation events for 4 weeks

**Stage 4: Maintenance (Months 4-12)**
- Tasks:
  - Monthly follow-ups
  - Quarterly BNP, kidney function
  - Annual echocardiogram
- Patient Education:
  - Lifelong adherence
  - Advanced care planning

#### AI Features:

**ML Model: hf-readmission-v3**
- Input: Demographics, LVEF, BNP, comorbidities, prior admissions, social factors
- Output: 30-day readmission probability
- Triggers: High-risk care pathway, home health services

**Early Decompensation Detector: hf-deterioration-v1**
- Input: Daily weights, symptoms (SOB, edema), vitals
- Output: Decompensation risk score
- Alerts: Weight gain trend, worsening symptoms, low SpO2

**Frontend Widget: Heart Failure Monitor**
```
Components:
- Daily Weight Chart (with trend line and alerts)
- Symptom Check-in Form (SOB scale, edema severity, fatigue)
- Medication Tracker (GDMT adherence)
- Fluid Intake/Output Log
- Activity Tolerance (6-minute walk distance)
- BNP Trend
- Next Appointment / Due Tasks
```

**Clinician View:**
```
- High-Risk Patient Queue (sorted by readmission risk)
- Weight Gain Alerts
- Missed Check-in Alerts
- GDMT Optimization Opportunities
```

#### Database Schema:

```sql
CREATE TABLE heart_failure_data (
    data_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    pathway_id UUID,
    record_date DATE NOT NULL,
    weight_kg DECIMAL(5,2),
    fluid_intake_ml INTEGER,
    fluid_output_ml INTEGER,
    shortness_of_breath_score INTEGER CHECK (shortness_of_breath_score BETWEEN 0 AND 10),
    edema_severity VARCHAR(20), -- NONE, MILD, MODERATE, SEVERE
    activity_tolerance VARCHAR(100),
    bnp_value DECIMAL(10,2),
    medications_taken JSONB,
    notes TEXT
);

CREATE INDEX idx_hf_patient ON heart_failure_data(patient_id);
CREATE INDEX idx_hf_date ON heart_failure_data(record_date);
```

#### Events Published:
- WeightGainAlertEvent
- HFDecompensationRiskEvent
- BNPElevatedEvent
- MedicationNonAdherenceEvent

---

### 1.4 Atrial Fibrillation (AFib)

**Prevalence:** ~2.7-6.1 million US adults
**Complexity:** High
**Pathway Duration:** 12 months

#### Care Pathway Stages:

**Stage 1: Diagnosis & Anticoagulation (Weeks 1-2)**
- Tasks:
  - ECG confirmation
  - Echocardiogram (assess LA size, valves, LVEF)
  - Thyroid function, electrolytes
  - CHA2DS2-VASc score calculation
  - Anticoagulation initiation (if score ≥1 for men, ≥2 for women)
- Patient Education:
  - AFib basics
  - Stroke risk
  - Anticoagulation importance

**Stage 2: Rate/Rhythm Control (Weeks 3-8)**
- Tasks:
  - Rate vs rhythm control decision
  - Beta-blocker or calcium channel blocker for rate
  - Cardioversion or antiarrhythmic for rhythm
  - Holter monitor or event recorder
- Patient Education:
  - Recognizing AFib symptoms
  - Medication adherence

**Stage 3: Optimization & Monitoring (Months 3-6)**
- Tasks:
  - INR monitoring (if warfarin) or annual kidney/liver function (if DOAC)
  - Symptom tracking
  - Wearable integration (Apple Watch, AliveCor)
  - Ablation consideration if symptomatic despite meds
- Milestone: Symptom-free or minimal symptoms for 3 months

**Stage 4: Long-Term Management (Months 7-12)**
- Tasks:
  - Quarterly follow-ups
  - Annual echocardiogram
  - Anticoagulation compliance monitoring
- Patient Education:
  - Lifelong anticoagulation
  - Bleeding risk awareness

#### AI Features:

**ML Model: afib-stroke-risk-v1**
- Input: CHA2DS2-VASc score, anticoagulation adherence, comorbidities
- Output: Stroke risk probability (augments traditional score with behavioral data)
- Alerts: Missed anticoagulation doses

**Irregular Rhythm Detector: afib-rhythm-v1**
- Input: Wearable data (Apple Watch, AliveCor), pulse irregularity
- Output: AFib episode detected (yes/no)
- Integration: Apple Health, AliveCor KardiaMobile

**Frontend Widget: AFib Control Dashboard**
```
Components:
- Rhythm Status (Normal Sinus Rhythm vs AFib)
- Episode Log (duration, symptoms, trigger)
- Anticoagulation Tracker (doses taken, INR values if warfarin)
- Stroke Risk Score (CHA2DS2-VASc with explanation)
- Wearable Data Integration (heart rate variability, irregularity detection)
- Bleeding Risk Assessment (HAS-BLED score)
```

#### Database Schema:

```sql
CREATE TABLE afib_episodes (
    episode_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    episode_start TIMESTAMP NOT NULL,
    episode_end TIMESTAMP,
    duration_minutes INTEGER,
    detected_by VARCHAR(50), -- ECG, WEARABLE, SYMPTOM
    symptoms TEXT[],
    heart_rate_avg INTEGER,
    intervention VARCHAR(100), -- NONE, CARDIOVERSION, MEDICATION
    notes TEXT
);

CREATE INDEX idx_afib_patient ON afib_episodes(patient_id);
CREATE INDEX idx_afib_start ON afib_episodes(episode_start);
```

---

### 1.5 Stroke / TIA (Secondary Prevention)

**Prevalence:** ~7.6 million stroke survivors in US
**Complexity:** High
**Pathway Duration:** 12 months

#### Care Pathway Stages:

**Stage 1: Immediate Post-Event (Weeks 1-4)**
- Tasks:
  - Determine stroke etiology (ischemic vs hemorrhagic, mechanism)
  - Initiate antiplatelet or anticoagulation
  - Statin high-intensity
  - BP control (target <130/80 after 24 hours)
  - Rehabilitation assessment (PT, OT, ST)
- Patient Education:
  - Stroke warning signs (FAST)
  - Risk factor modification
  - Rehabilitation importance

**Stage 2: Rehabilitation & Risk Factor Control (Months 2-6)**
- Tasks:
  - PT/OT/ST sessions
  - Monthly provider check-ins
  - BP, lipid, glucose monitoring
  - Smoking cessation if applicable
  - Carotid imaging if indicated
- Patient Education:
  - Lifestyle modifications
  - Medication adherence
- Milestone: Functional improvement, risk factors optimized

**Stage 3: Long-Term Prevention (Months 7-12)**
- Tasks:
  - Quarterly follow-ups
  - Annual imaging (carotid ultrasound, brain MRI)
  - Risk factor monitoring
- Patient Education:
  - Lifelong secondary prevention
  - Recognizing recurrent stroke

#### AI Features:

**ML Model: stroke-recurrence-v1**
- Input: Stroke type, risk factors (HTN, DM, AFib), adherence, functional status
- Output: 5-year recurrence risk
- Alerts: Uncontrolled BP, medication non-adherence

**Frontend Widget: Stroke Prevention Dashboard**
```
Components:
- Risk Factor Dashboard (BP, lipids, HbA1c, smoking status)
- Medication Adherence Tracker (antiplatelet, statin, BP meds)
- Rehabilitation Progress (functional assessments, mobility)
- Next Imaging Due
- FAST Education (always visible)
```

---

### 1.6 High Cholesterol / Dyslipidemia

**Prevalence:** ~93 million US adults with high cholesterol
**Complexity:** Low-Moderate
**Pathway Duration:** 6 months

#### Care Pathway Stages:

**Stage 1: Baseline Assessment (Week 1)**
- Tasks:
  - Complete lipid panel (fasting)
  - 10-year ASCVD risk calculation
  - Liver function tests (baseline)
  - Lifestyle assessment
- Patient Education:
  - Cholesterol basics
  - Diet (Mediterranean, low saturated fat)
  - Exercise

**Stage 2: Intervention Initiation (Weeks 2-8)**
- Tasks:
  - Statin initiation (intensity based on ASCVD risk)
  - Lifestyle counseling
  - LFTs at 4 weeks
- Patient Education:
  - Medication adherence
  - Muscle pain awareness (statin myopathy)

**Stage 3: Optimization (Months 3-6)**
- Tasks:
  - Lipid panel at 3 months
  - Dose adjustment if not at goal
  - Consider ezetimibe or PCSK9 inhibitor if needed
- Milestone: LDL at goal for risk category

#### AI Features:

**ML Model: lipid-response-v1**
- Input: Baseline lipids, statin type/dose, adherence
- Output: Predicted LDL reduction
- Alerts: Overdue lab work (>6 months without lipid panel)

**Frontend Widget: Cholesterol Tracker**
```
Components:
- Lipid Trend Chart (LDL, HDL, Triglycerides)
- Goal Progress (LDL target based on ASCVD risk)
- Medication Adherence
- Diet Log (saturated fat intake)
- Next Lab Due
```

#### Database Schema:

```sql
CREATE TABLE lipid_panels (
    panel_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    test_date DATE NOT NULL,
    total_cholesterol INTEGER,
    ldl_cholesterol INTEGER,
    hdl_cholesterol INTEGER,
    triglycerides INTEGER,
    non_hdl_cholesterol INTEGER,
    fasting BOOLEAN,
    lab_source VARCHAR(100)
);

CREATE INDEX idx_lipids_patient ON lipid_panels(patient_id);
CREATE INDEX idx_lipids_date ON lipid_panels(test_date);
```

---

## 🩺 Category 2: Metabolic & Kidney (5 Conditions)

### 2.1 Type 2 Diabetes

**Prevalence:** ~37.3 million Americans (11.3% of population)
**Complexity:** High
**Pathway Duration:** 12 months (then ongoing)

#### Care Pathway Stages:

**Stage 1: Diagnosis & Education (Month 1)**
- Tasks:
  - HbA1c baseline, lipid panel, kidney function (eGFR, UACR), liver function
  - Diabetic retinopathy screening (dilated eye exam)
  - Foot examination (monofilament, pulses)
  - Diabetes self-management education (DSME) referral
  - Medication initiation (Metformin first-line unless contraindicated)
- Patient Education:
  - Diabetes basics (what is T2DM, complications)
  - Blood glucose monitoring (if applicable)
  - Hypoglycemia recognition and treatment
  - Sick day management

**Stage 2: Medication Titration (Months 2-3)**
- Tasks:
  - Weekly glucose monitoring (fasting, pre-meal, or CGM if available)
  - Metformin dose escalation (target 2000mg/day if tolerated)
  - HbA1c recheck at 3 months
  - Add 2nd agent if HbA1c not at goal (<7% for most, individualized)
- Patient Education:
  - Medication adherence strategies
  - GI side effects management (Metformin)
  - Nutrition counseling (carb counting, plate method)
  - Physical activity goals (150 min/week)

**Stage 3: Optimization (Months 4-6)**
- Tasks:
  - HbA1c every 3 months until at goal
  - Medication intensification (GLP-1 agonist, SGLT2 inhibitor, DPP4 inhibitor, insulin)
  - Continuous glucose monitoring (CGM) consideration
  - Complication screening:
    - Annual comprehensive foot exam
    - Annual dilated eye exam
    - Biannual kidney function (eGFR, UACR)
    - Annual lipid panel
- Patient Education:
  - Long-term complications prevention
  - Self-monitoring blood glucose (SMBG) technique
  - Insulin injection technique (if applicable)
- Milestone: HbA1c at individualized goal

**Stage 4: Maintenance (Months 7-12 and ongoing)**
- Tasks:
  - HbA1c every 3-6 months
  - Quarterly provider visits
  - Annual comprehensive diabetic exam (ADA standards)
  - Medication adherence monitoring
  - Weight management
- Patient Education:
  - Lifelong adherence
  - Preventing hypoglycemia
  - Sick day rules
  - Travel with diabetes

#### AI Features:

**ML Model: diabetes-complication-v1**
- Input: HbA1c trajectory, duration of diabetes, BP, lipids, kidney function, smoking status
- Output: 5-year risk of:
  - Diabetic retinopathy
  - Diabetic nephropathy progression
  - Diabetic neuropathy
  - Cardiovascular events
- Alerts: HbA1c >9% (poorly controlled), rapid progression

**Glucose Trend Predictor: glucose-forecast-v1** (if CGM data available)
- Input: CGM time-series data, meal timing, activity, medications
- Output: Predicted glucose trajectory next 1-4 hours
- Alerts: Hypoglycemia risk (predicted <70 mg/dL), hyperglycemia risk (>250 mg/dL)

**Meal Impact Predictor: meal-glucose-v1**
- Input: Meal photo + carb estimate, current glucose, active insulin
- Output: Predicted glucose excursion
- Action: Insulin dose recommendation (if on insulin)

**Frontend Widget: Diabetes Control Center**
```
Components:
- Glucose Dashboard:
  - If CGM: Real-time glucose, trend arrow, time in range (70-180 mg/dL)
  - If SMBG: Recent readings, average glucose, estimated A1c
- HbA1c Trend Chart (target line, actual values over time)
- Medication Adherence Tracker (Metformin, GLP-1, insulin doses)
- Meal & Activity Logger:
  - Carb counter
  - Exercise minutes
  - Photo food diary
- Complication Screening Checklist:
  - Eye exam due (annual)
  - Foot exam due (annual)
  - Kidney function due (biannual)
  - Lipid panel due (annual)
- Hypoglycemia Event Log
- Education Module Completion Progress
```

**Clinician View:**
```
- Diabetic Patient Cohort:
  - Patients by HbA1c control:
    - Well-controlled (<7%)
    - Moderate (7-9%)
    - Poor (>9%)
  - Sort by: Last HbA1c, days since last visit, complications present
- Overdue Screening Alerts:
  - Eye exam overdue
  - Foot exam overdue
  - Kidney function overdue
- Medication Optimization Opportunities:
  - Not on SGLT2i (if CKD or HF)
  - Not on GLP-1 (if BMI >30 or ASCVD)
- Hypoglycemia Event Review Queue
```

#### Database Schema:

```sql
CREATE TABLE diabetes_data (
    data_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    pathway_id UUID,
    record_timestamp TIMESTAMP NOT NULL,
    glucose_mg_dl INTEGER,
    measurement_type VARCHAR(50), -- FASTING, RANDOM, PRE_MEAL, POST_MEAL, BEDTIME, CGM
    carb_intake_grams INTEGER,
    meal_description TEXT,
    exercise_minutes INTEGER,
    insulin_dose_units DECIMAL(5,2),
    insulin_type VARCHAR(50),
    hypoglycemia_event BOOLEAN DEFAULT false,
    notes TEXT
);

CREATE TABLE diabetes_complications_screening (
    screening_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    screening_type VARCHAR(100) NOT NULL, -- EYE_EXAM, FOOT_EXAM, KIDNEY_FUNCTION, MONOFILAMENT
    screening_date DATE NOT NULL,
    result VARCHAR(50),
    findings TEXT,
    next_due_date DATE
);

CREATE INDEX idx_diabetes_patient ON diabetes_data(patient_id);
CREATE INDEX idx_diabetes_timestamp ON diabetes_data(record_timestamp);
CREATE INDEX idx_screening_patient ON diabetes_complications_screening(patient_id);
CREATE INDEX idx_screening_type ON diabetes_complications_screening(screening_type);
```

#### Events Published:
- GlucoseReadingRecordedEvent
- HypoglycemiaDetectedEvent
- HbA1cGoalAchievedEvent
- ComplicationScreeningOverdueEvent
- MedicationAdjustmentNeededEvent

---

### 2.2 Obesity / Metabolic Syndrome

**Prevalence:** ~42% of US adults obese (BMI ≥30)
**Complexity:** Moderate-High
**Pathway Duration:** 12 months (intensive), then ongoing

#### Care Pathway Stages:

**Stage 1: Assessment & Goal Setting (Weeks 1-2)**
- Tasks:
  - Baseline weight, BMI, waist circumference
  - Metabolic panel: Lipids, HbA1c, liver function
  - Blood pressure
  - Assess for obesity-related comorbidities (T2DM, HTN, OSA, NAFLD)
  - Calculate metabolic age
  - Set realistic weight loss goal (5-10% body weight over 6 months)
- Patient Education:
  - Health risks of obesity
  - Benefits of 5-10% weight loss
  - Behavioral strategies
  - Nutrition basics (calorie deficit, portion control)

**Stage 2: Intensive Behavioral Intervention (Months 1-6)**
- Tasks:
  - Weekly check-ins (group or individual)
  - Daily food logging (app-based)
  - Daily activity tracking (step count goal: 7,000-10,000/day)
  - Bi-weekly weigh-ins
  - Medication consideration (GLP-1 agonist, orlistat, phentermine-topiramate)
  - Bariatric surgery evaluation if BMI >40 or >35 with comorbidities
- Patient Education:
  - Calorie tracking
  - Mindful eating
  - Emotional eating triggers
  - Exercise progression (start 30 min/day, build to 60 min/day)
  - Sleep hygiene (7-9 hours/night)
- Milestone: 5% weight loss by month 6

**Stage 3: Weight Maintenance (Months 7-12)**
- Tasks:
  - Monthly check-ins
  - Weekly weigh-ins (self-monitoring)
  - Continued activity tracking
  - Relapse prevention strategies
- Patient Education:
  - Maintaining weight loss (80% of lost weight regained without ongoing intervention)
  - Lifelong lifestyle changes
  - Recognizing relapse early
- Milestone: Maintain weight loss (no more than 3% regain)

#### AI Features:

**ML Model: weight-trajectory-v1**
- Input: Baseline weight, adherence to food logging, activity levels, medication use
- Output: Predicted weight at 3, 6, 12 months
- Alerts: Weight plateau >4 weeks, weight regain >5 lbs

**Relapse Risk Detector: obesity-relapse-v1**
- Input: Frequency of food logging, activity levels, weight trend, self-efficacy scores
- Output: Relapse risk probability
- Triggers: Intensified coaching, motivational interviewing

**Frontend Widget: Weight Management Dashboard**
```
Components:
- Weight Trend Chart (goal trajectory vs actual)
- BMI Tracker
- Food Diary (daily calorie intake, macronutrient breakdown)
- Activity Tracker (steps, exercise minutes, calories burned)
- Medication Tracker (if on weight loss medication)
- Behavioral Goals Checklist (weekly goals: meal prep, no late-night snacking, etc.)
- Progress Milestones (5% loss, 10% loss)
- Body Measurements (waist, hip, body fat %)
```

---

### 2.3 Chronic Kidney Disease (CKD)

**Prevalence:** ~37 million US adults with CKD
**Complexity:** High
**Pathway Duration:** Ongoing (lifelong)

#### Care Pathway Stages:

**Stage 1: Diagnosis & Staging (Weeks 1-4)**
- Tasks:
  - Confirm CKD: eGFR <60 for >3 months OR albuminuria (UACR >30 mg/g) for >3 months
  - Determine CKD stage (1-5 based on eGFR)
  - Identify cause (diabetes, hypertension, glomerular disease, etc.)
  - Nephrology referral if eGFR <30 or rapid progression
  - Baseline labs: CMP, CBC, PTH, vitamin D, phosphorus
- Patient Education:
  - CKD basics
  - Importance of BP and glucose control
  - Avoiding nephrotoxins (NSAIDs, contrast dye)

**Stage 2: Slow Progression (Ongoing)**
- Tasks (frequency based on CKD stage):
  - eGFR and UACR monitoring:
    - Stage 1-2: Annually
    - Stage 3: Every 6 months
    - Stage 4-5: Every 3 months
  - BP control (target <130/80, lower if albuminuria)
  - ACE inhibitor or ARB if albuminuria present
  - SGLT2 inhibitor (Jardiance, Farxiga) for CKD protection
  - Treat complications:
    - Anemia (ESA, iron)
    - Bone disease (phosphate binders, vitamin D, calcimimetics)
    - Metabolic acidosis (sodium bicarbonate)
  - Dietary protein restriction (0.8 g/kg/day)
  - Smoking cessation
- Patient Education:
  - Low-sodium, low-potassium, low-phosphorus diet
  - Medication safety (avoid NSAIDs, adjust doses for kidney function)
  - Recognizing uremia symptoms

**Stage 3: Preparation for Kidney Replacement (if Stage 4-5)**
- Tasks:
  - Dialysis education
  - Vascular access planning (AV fistula creation if hemodialysis)
  - Transplant evaluation
  - Advance care planning
- Patient Education:
  - Dialysis modalities (hemodialysis, peritoneal dialysis)
  - Transplant process
  - Conservative management option

#### AI Features:

**ML Model: ckd-progression-v1**
- Input: Baseline eGFR, rate of decline, albuminuria, BP control, diabetes control, medications
- Output: Predicted time to ESRD (eGFR <15) or dialysis
- Alerts: Rapid decline (eGFR drop >5 mL/min/year), nephrologist referral needed

**Drug Dosing Safety: renal-dose-checker-v1**
- Input: eGFR, medication list
- Output: Dose adjustments needed, contraindicated medications
- Integration: Prescription service safety checks

**Frontend Widget: CKD Monitor**
```
Components:
- eGFR Trend Chart (with stage thresholds: 90, 60, 45, 30, 15)
- Albuminuria Trend (UACR over time)
- BP Control Dashboard (target <130/80)
- Medication List (ACEi/ARB, SGLT2i, diuretics with renal dosing)
- Labs Due (eGFR, UACR, PTH, CBC)
- Diet Tracker (protein, potassium, phosphorus intake)
- Dialysis Preparation Checklist (if Stage 4-5)
```

---

### 2.4 Thyroid Disorders

**Prevalence:** ~20 million Americans
**Complexity:** Low-Moderate
**Pathway Duration:** 6 months initial, then ongoing

#### Care Pathway Stages:

**Stage 1: Diagnosis (Weeks 1-2)**
- Tasks:
  - TSH, Free T4, Free T3 (if indicated)
  - Thyroid antibodies (TPO, TG) if autoimmune suspected
  - Ultrasound if nodule palpated
  - Determine type (hypothyroidism, hyperthyroidism, subclinical)
- Patient Education:
  - Thyroid function basics
  - Symptoms of hypo/hyperthyroidism

**Stage 2: Medication Initiation (Weeks 3-8)**
- Tasks:
  - Levothyroxine for hypothyroidism (start low, titrate slowly)
  - Methimazole or PTU for hyperthyroidism
  - TSH recheck at 6 weeks
  - Dose adjustment
- Patient Education:
  - Take levothyroxine on empty stomach, 1 hour before food
  - Medication adherence
  - Symptom tracking

**Stage 3: Optimization (Months 3-6)**
- Tasks:
  - TSH every 6 weeks until stable
  - Target TSH: 0.5-2.5 mIU/L (individualized)
  - Monitor for overtreatment (hyperthyroid symptoms) or undertreatment
- Milestone: TSH at goal for 2 consecutive checks

#### AI Features:

**ML Model: thyroid-dose-predictor-v1**
- Input: TSH, Free T4, weight, age, current levothyroxine dose
- Output: Recommended dose adjustment
- Alerts: TSH <0.1 (over-replaced), TSH >10 (severely under-replaced)

**Frontend Widget: Thyroid Tracker**
```
Components:
- TSH Trend Chart (with goal range shaded)
- Symptom Log (fatigue, weight changes, palpitations, cold/heat intolerance)
- Medication Adherence (levothyroxine doses taken)
- Next Lab Due
```

---

### 2.5 Gout

**Prevalence:** ~9.2 million US adults
**Complexity:** Moderate
**Pathway Duration:** 6 months

#### Care Pathway Stages:

**Stage 1: Acute Flare Management (Days 1-14)**
- Tasks:
  - NSAIDs, colchicine, or corticosteroids for acute pain
  - Joint fluid aspiration if diagnosis uncertain (confirm monosodium urate crystals)
  - Serum uric acid level
  - Assess for triggers (alcohol, high-purine foods, diuretics)
- Patient Education:
  - Gout flare management
  - Ice, elevation, rest
  - Triggers to avoid

**Stage 2: Urate-Lowering Therapy (Weeks 3-12)**
- Tasks:
  - Allopurinol or febuxostat initiation (start low dose)
  - Target uric acid <6 mg/dL (<5 mg/dL if tophi present)
  - Uric acid recheck at 4 weeks
  - Prophylactic colchicine or NSAID for 3-6 months (prevent flares during ULT initiation)
- Patient Education:
  - Lifelong ULT (not just during flares)
  - Dietary modifications (limit alcohol, high-fructose corn syrup, organ meats)
  - Hydration (2-3 L/day)

**Stage 3: Maintenance (Months 4-6)**
- Tasks:
  - Uric acid every 3 months
  - Dose titration to goal
  - Monitor for flares (frequency should decrease)
- Milestone: Uric acid at goal for 3 months, no flares

#### AI Features:

**ML Model: gout-flare-predictor-v1**
- Input: Uric acid levels, medication adherence, dietary triggers, alcohol intake, diuretic use
- Output: Flare risk probability next 30 days
- Alerts: Rising uric acid, medication non-adherence

**Frontend Widget: Gout Control Panel**
```
Components:
- Uric Acid Trend Chart (with goal line at 6 mg/dL)
- Flare Log (date, severity, duration, trigger)
- Medication Tracker (allopurinol adherence)
- Trigger Tracker (alcohol, high-purine meals, dehydration)
- Next Uric Acid Test Due
```

---

## 🫁 Category 3: Respiratory (4 Conditions)

### 3.1 Asthma

**Prevalence:** ~25 million Americans
**Complexity:** Moderate
**Pathway Duration:** 6 months

#### Care Pathway Stages:

**Stage 1: Diagnosis & Severity Assessment (Weeks 1-2)**
- Tasks:
  - Spirometry (confirm airflow obstruction, reversibility with bronchodilator)
  - Asthma Control Test (ACT) score
  - Determine severity (intermittent, mild, moderate, severe persistent)
  - Identify triggers (allergens, exercise, cold air, infections)
  - Create Asthma Action Plan (green/yellow/red zones)
- Patient Education:
  - Asthma basics (inflammation, bronchoconstriction)
  - Inhaler technique (MDI with spacer, DPI, nebulizer)
  - Trigger avoidance

**Stage 2: Medication Initiation (Weeks 3-8)**
- Tasks:
  - SABA (albuterol) for all patients (rescue)
  - ICS (inhaled corticosteroid) for persistent asthma
  - Add LABA if moderate-severe persistent
  - Peak flow monitoring (optional, useful for some)
  - Weekly symptom check-ins
- Patient Education:
  - Controller vs rescue inhaler
  - When to use rescue inhaler (pre-exercise, acute symptoms)
  - Recognizing worsening asthma

**Stage 3: Optimization (Months 3-6)**
- Tasks:
  - ACT score monthly
  - Spirometry at 3-6 months
  - Step up/down therapy based on control
  - Consider biologic therapy (omalizumab, dupilumab) if severe uncontrolled
  - Allergy testing and immunotherapy if indicated
- Milestone: Well-controlled asthma (ACT >20, minimal rescue inhaler use, no exacerbations)

#### AI Features:

**ML Model: asthma-exacerbation-v1**
- Input: Peak flow trends, rescue inhaler use frequency, symptoms, environmental factors (pollen count, air quality)
- Output: Exacerbation risk next 7 days
- Alerts: Increased rescue use (>2 times/week), declining peak flow

**Environmental Trigger Analyzer: asthma-trigger-v1**
- Input: Symptom logs, location data, weather/pollen/air quality data
- Output: Personalized trigger identification
- Action: Avoidance recommendations, pre-treatment suggestions

**Frontend Widget: Asthma Control Center**
```
Components:
- ACT Score Tracker (monthly, target >20)
- Peak Flow Diary (morning PEF, if applicable)
- Inhaler Usage Log:
  - Rescue inhaler (albuterol) puffs/day
  - Controller inhaler (ICS/LABA) adherence
- Symptom Check-in:
  - Daytime symptoms
  - Nighttime awakenings
  - Activity limitation
- Trigger Log (exposure to allergens, cold air, exercise)
- Asthma Action Plan (color-coded):
  - Green Zone: Doing well
  - Yellow Zone: Caution (increase controller, use rescue)
  - Red Zone: Medical alert (seek emergency care)
- Environmental Data (pollen count, air quality index)
```

**Clinician View:**
```
- Asthma Cohort:
  - Well-controlled (ACT >20)
  - Partially controlled (ACT 16-19)
  - Uncontrolled (ACT <16)
- Exacerbation Risk Queue
- Rescue Inhaler Overuse Alerts (>2x/week)
- Non-Adherence Alerts (controller medication)
```

#### Database Schema:

```sql
CREATE TABLE asthma_data (
    data_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    pathway_id UUID,
    record_date DATE NOT NULL,
    peak_flow_lpm INTEGER,
    rescue_inhaler_puffs INTEGER,
    controller_taken BOOLEAN,
    daytime_symptoms INTEGER CHECK (daytime_symptoms BETWEEN 0 AND 7), -- days/week
    nighttime_awakenings INTEGER, -- nights/week
    activity_limitation VARCHAR(50), -- NONE, SOME, MAJOR
    triggers_exposed TEXT[],
    act_score INTEGER CHECK (act_score BETWEEN 5 AND 25),
    notes TEXT
);

CREATE INDEX idx_asthma_patient ON asthma_data(patient_id);
CREATE INDEX idx_asthma_date ON asthma_data(record_date);
```

---

### 3.2 COPD (Chronic Obstructive Pulmonary Disease)

**Prevalence:** ~16 million diagnosed (likely many more undiagnosed)
**Complexity:** High
**Pathway Duration:** 12 months (then ongoing)

#### Care Pathway Stages:

**Stage 1: Diagnosis & Staging (Weeks 1-4)**
- Tasks:
  - Spirometry (confirm FEV1/FVC <0.70)
  - Determine GOLD stage (1-4 based on FEV1% predicted)
  - CAT score (COPD Assessment Test)
  - Exacerbation history (frequency, severity)
  - Pulse oximetry (assess for hypoxemia)
  - Smoking status and cessation counseling
- Patient Education:
  - COPD basics (emphysema, chronic bronchitis)
  - Smoking cessation (most important intervention)
  - Recognizing exacerbations

**Stage 2: Medication Optimization (Months 1-3)**
- Tasks:
  - LAMA (long-acting muscarinic antagonist) or LABA for symptomatic patients
  - Combination LAMA/LABA if more symptomatic
  - ICS if frequent exacerbations (≥2/year)
  - Pulmonary rehabilitation referral
  - Influenza and pneumococcal vaccines
- Patient Education:
  - Inhaler technique
  - Difference between maintenance and rescue inhalers
  - Energy conservation techniques

**Stage 3: Monitoring & Exacerbation Prevention (Months 4-12)**
- Tasks:
  - Quarterly provider visits
  - Annual spirometry
  - Oxygen assessment (consider long-term oxygen if SpO2 <88% at rest)
  - Exacerbation action plan (when to start antibiotics/steroids)
- Milestone: <2 exacerbations per year, improved functional status

#### AI Features:

**ML Model: copd-exacerbation-v1**
- Input: Symptom trends (dyspnea, sputum color/volume), rescue inhaler use, FEV1, weather changes
- Output: Exacerbation risk next 14 days
- Alerts: Early exacerbation signs, trigger rescue pack

**Rescue Pack Decision Support:**
- Input: Symptoms (Anthonisen criteria: increased dyspnea, sputum volume, sputum purulence)
- Output: Recommendation to start antibiotics + steroids
- Action: Notify provider, dispense rescue pack

**Frontend Widget: COPD Management Dashboard**
```
Components:
- Symptom Tracker:
  - mMRC Dyspnea Scale (0-4)
  - Sputum color chart
  - Sputum volume
- CAT Score (monthly, 0-40 scale)
- Spirometry Results (FEV1% predicted over time)
- Medication Adherence (maintenance inhalers)
- Pulse Oximetry (SpO2)
- Exacerbation Log (date, severity, treatment)
- Pulmonary Rehab Progress
- Smoking Cessation Support
```

---

### 3.3 Chest Infection / Pneumonia (Recovery)

**Complexity:** Moderate
**Pathway Duration:** 4-6 weeks

#### Care Pathway Stages:

**Stage 1: Diagnosis & Treatment (Days 1-3)**
- Tasks:
  - Chest X-ray (confirm pneumonia)
  - Assess severity (outpatient vs inpatient, CURB-65 score)
  - Antibiotic initiation (based on likely pathogen)
  - Supportive care (hydration, oxygen if needed)
- Patient Education:
  - Complete full antibiotic course
  - Hydration importance
  - When to seek emergency care (worsening dyspnea, confusion)

**Stage 2: Monitoring & Recovery (Days 4-14)**
- Tasks:
  - Daily symptom check-ins
  - Fever tracking
  - Oxygen saturation monitoring
  - Antibiotic completion (typically 5-7 days for CAP)
  - Follow-up appointment at end of antibiotics
- Patient Education:
  - Expected recovery timeline (4-6 weeks for full recovery)
  - Gradual return to activities
  - Smoking cessation (if applicable)

**Stage 3: Follow-Up & Prevention (Weeks 3-6)**
- Tasks:
  - Repeat chest X-ray at 6 weeks (if >50 yo or smoker, to rule out underlying malignancy)
  - Pneumococcal vaccine if not up to date
  - Influenza vaccine (seasonal)
- Milestone: Symptom resolution, X-ray clearance

#### AI Features:

**ML Model: pneumonia-severity-v1**
- Input: CURB-65 components, vitals, oxygen saturation, comorbidities
- Output: Inpatient vs outpatient recommendation, mortality risk
- Alerts: Clinical deterioration (increased oxygen need, persistent fever >72 hours)

**Frontend Widget: Pneumonia Recovery Tracker**
```
Components:
- Symptom Severity Chart (cough, dyspnea, fatigue - scale 0-10)
- Temperature Log (fever tracking)
- SpO2 Monitoring
- Antibiotic Completion Tracker
- Activity Tolerance (improving, stable, worsening)
- Follow-Up Checklist (repeat CXR, vaccines)
```

---

### 3.4 Long COVID / Post-Viral Fatigue

**Prevalence:** Estimated 10-30% of COVID-19 survivors
**Complexity:** High (poorly understood, multisystem)
**Pathway Duration:** 6-12 months

#### Care Pathway Stages:

**Stage 1: Assessment & Symptom Mapping (Weeks 1-4)**
- Tasks:
  - Comprehensive symptom inventory (fatigue, dyspnea, brain fog, POTS, etc.)
  - Baseline functional assessment (6-minute walk test, cognitive testing)
  - Rule out alternative diagnoses (pulmonary embolism, myocarditis, etc.)
  - Pulmonary function tests if dyspnea
  - Echocardiogram if cardiac symptoms
  - Labs: CBC, CMP, inflammatory markers, D-dimer, troponin
- Patient Education:
  - Long COVID basics (definition, symptoms, prognosis)
  - Pacing and energy envelope theory (avoid post-exertional malaise)
  - Symptom journaling

**Stage 2: Symptom Management & Rehabilitation (Months 2-6)**
- Tasks:
  - Graded exercise therapy (very gradual, avoid PEM)
  - Cognitive rehabilitation if brain fog
  - Treat POTS if present (increase salt/fluids, compression stockings, beta-blockers)
  - Manage sleep disturbance
  - Mental health support (anxiety, depression common)
  - Occupational therapy for activity pacing
- Patient Education:
  - Recognizing post-exertional malaise (PEM)
  - Rest before exhaustion
  - Energy budgeting
- Milestone: Gradual functional improvement

**Stage 3: Long-Term Monitoring (Months 7-12)**
- Tasks:
  - Monthly symptom tracking
  - Functional reassessment (6MWT)
  - Return to work planning (gradual, accommodations)
  - Ongoing multidisciplinary support
- Patient Education:
  - Setbacks are common
  - Pacing is lifelong skill

#### AI Features:

**ML Model: long-covid-trajectory-v1**
- Input: Initial symptom severity, duration since infection, functional baseline, comorbidities
- Output: Predicted recovery trajectory (rapid, gradual, persistent)
- Alerts: PEM triggers, functional decline

**Pacing Coach: activity-envelope-v1**
- Input: Daily activity logs, symptom severity, heart rate variability
- Output: Personalized activity recommendations (safe activity window)
- Action: Prevent overexertion

**Frontend Widget: Long COVID Symptom Tracker**
```
Components:
- Multi-Symptom Tracker:
  - Fatigue (0-10)
  - Dyspnea (0-10)
  - Brain fog (0-10)
  - Palpitations (yes/no)
  - Dizziness (yes/no)
  - Pain (location, severity)
- Activity Log (steps, exercise, work hours)
- PEM Event Log (date, trigger, duration)
- Energy Envelope Graph (activity vs capacity over time)
- Heart Rate Variability (if wearable data available)
- Functional Status (6-minute walk distance trend)
- Return to Work Progress
```

---

## 🧠 Category 4: Mental Health & Neuro (7 Conditions)

### 4.1 Depression

**Prevalence:** ~21 million US adults with major depressive episode
**Complexity:** Moderate-High
**Pathway Duration:** 6 months (acute), then ongoing

#### Care Pathway Stages:

**Stage 1: Assessment & Safety (Weeks 1-2)**
- Tasks:
  - PHQ-9 (Patient Health Questionnaire-9) baseline
  - Suicidal ideation screening (Columbia Suicide Severity Rating Scale)
  - Assess severity (mild, moderate, moderately severe, severe)
  - Rule out bipolar disorder (MDQ - Mood Disorder Questionnaire)
  - Medical workup (TSH, B12, CBC - rule out medical causes)
  - Create safety plan if suicidal ideation present
  - Psychiatry/therapy referral
- Patient Education:
  - Depression basics (biological illness, not weakness)
  - Treatment options (medication, therapy, combination)
  - Safety plan

**Stage 2: Treatment Initiation (Weeks 3-8)**
- Tasks:
  - Antidepressant initiation (SSRI or SNRI first-line)
  - Psychotherapy enrollment (CBT, IPT, or other evidence-based)
  - Weekly PHQ-9 check-ins
  - Side effect monitoring
  - Suicide risk reassessment weekly
- Patient Education:
  - Medications take 4-6 weeks for full effect
  - Don't stop abruptly
  - Therapy attendance importance
  - Crisis resources (988 Suicide & Crisis Lifeline)

**Stage 3: Optimization (Months 3-6)**
- Tasks:
  - PHQ-9 every 2 weeks
  - Dose titration if partial response
  - Switch or augment if no response by 8 weeks
  - Continued therapy
  - Medication adherence monitoring
- Milestone: Remission (PHQ-9 <5 for 2 consecutive visits)

**Stage 4: Continuation & Relapse Prevention (Months 7-12)**
- Tasks:
  - Monthly PHQ-9
  - Continue antidepressant for at least 6 months after remission
  - Ongoing therapy (taper frequency)
  - Relapse prevention planning
- Patient Education:
  - High relapse risk if stop meds early
  - Recognizing early warning signs
  - Maintenance strategies

#### AI Features:

**ML Model: depression-relapse-v1**
- Input: PHQ-9 scores over time, medication adherence, therapy attendance, life stressors, sleep quality
- Output: Relapse risk probability next 30 days
- Alerts: PHQ-9 worsening, missed therapy sessions, medication non-adherence

**Suicidal Ideation Detector: si-risk-v1**
- Input: PHQ-9 item 9, C-SSRS scores, text analysis of check-in responses (NLP for suicidal language)
- Output: Suicide risk level (low, moderate, high)
- Action: Immediate human review (HIGH priority), safety plan activation, crisis line offer

**Frontend Widget: Depression Management Dashboard**
```
Components:
- PHQ-9 Tracker (weekly/biweekly scores, trend chart)
- Mood Log (daily mood rating 1-10, optional journaling)
- Medication Adherence Tracker
- Therapy Session Tracker (sessions attended, next appointment)
- Sleep Log (hours, quality)
- Activity Tracker (social activities, exercise - behavioral activation)
- Safety Plan (always accessible):
  - Warning signs
  - Coping strategies
  - Social contacts
  - Crisis numbers (988, therapist, emergency)
- Positive Activity Scheduling
- Gratitude/Thought Log (CBT exercises)
```

**Clinician View:**
```
- Depression Cohort:
  - In remission (PHQ-9 <5)
  - Partial response (PHQ-9 5-9)
  - Minimal response (PHQ-9 10-14)
  - Not improved (PHQ-9 ≥15)
- Suicide Risk Alerts (HIGH PRIORITY)
- PHQ-9 Worsening Alerts
- Non-Adherence Alerts (meds, therapy)
- Review Queue (patients requiring check-in)
```

#### Database Schema:

```sql
CREATE TABLE depression_data (
    data_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    pathway_id UUID,
    record_date DATE NOT NULL,
    phq9_score INTEGER CHECK (phq9_score BETWEEN 0 AND 27),
    phq9_item_9 INTEGER CHECK (phq9_item_9 BETWEEN 0 AND 3), -- Suicidal ideation question
    mood_rating INTEGER CHECK (mood_rating BETWEEN 1 AND 10),
    sleep_hours DECIMAL(3,1),
    medication_taken BOOLEAN,
    therapy_session_attended BOOLEAN,
    suicidal_ideation BOOLEAN,
    safety_plan_reviewed BOOLEAN,
    notes TEXT
);

CREATE TABLE safety_plans (
    plan_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    warning_signs TEXT[],
    coping_strategies TEXT[],
    social_contacts JSONB,
    professional_contacts JSONB,
    safe_environment_steps TEXT[]
);

CREATE INDEX idx_depression_patient ON depression_data(patient_id);
CREATE INDEX idx_depression_date ON depression_data(record_date);
CREATE INDEX idx_depression_si ON depression_data(suicidal_ideation);
```

#### Events Published:
- PHQ9CompletedEvent
- SuicidalIdeationDetectedEvent (CRITICAL)
- DepressionRemissionAchievedEvent
- MedicationNonAdherenceEvent
- TherapyMissedEvent

#### Safety Guardrails:
- ANY suicidal ideation (PHQ-9 item 9 >0) → Immediate clinician notification
- PHQ-9 ≥20 (severe) → Urgent review within 24 hours
- Worsening PHQ-9 (+5 points) → Review within 48 hours

---

### 4.2 Anxiety Disorders

**Prevalence:** ~31% of US adults experience anxiety disorder at some point
**Complexity:** Moderate
**Pathway Duration:** 6 months

#### Care Pathway Stages:

**Stage 1: Assessment (Weeks 1-2)**
- Tasks:
  - GAD-7 (Generalized Anxiety Disorder-7) baseline
  - Determine type (GAD, panic disorder, social anxiety, specific phobia)
  - Rule out medical causes (hyperthyroidism, cardiac, substance use)
  - Therapy referral (CBT is first-line)
- Patient Education:
  - Anxiety basics (fight-or-flight response)
  - Treatment options
  - Relaxation techniques (deep breathing, progressive muscle relaxation)

**Stage 2: Treatment (Weeks 3-12)**
- Tasks:
  - CBT sessions (weekly)
  - Medication if moderate-severe (SSRI/SNRI)
  - Weekly GAD-7 check-ins
  - Exposure therapy if indicated (for phobias, panic)
- Patient Education:
  - Cognitive restructuring
  - Exposure principles
  - Mindfulness techniques

**Stage 3: Maintenance (Months 4-6)**
- Tasks:
  - Biweekly GAD-7
  - Taper therapy frequency
  - Relapse prevention
- Milestone: GAD-7 <10 for 8 weeks

#### AI Features:

**ML Model: anxiety-trigger-v1**
- Input: Anxiety episode logs, situational triggers, physiological data (HR from wearable)
- Output: Personalized trigger identification
- Action: Avoidance strategies, pre-exposure preparation

**Panic Attack Predictor: panic-forecast-v1**
- Input: HRV (heart rate variability), breathing rate, self-reported anxiety level
- Output: Panic attack risk next 1 hour
- Action: Trigger grounding exercise, breathing timer

**Frontend Widget: Anxiety Toolkit**
```
Components:
- GAD-7 Tracker (weekly scores)
- Anxiety Episode Log (date, severity, trigger, duration)
- Breathing Timer (box breathing, 4-7-8 breathing)
- Grounding Exercises:
  - 5-4-3-2-1 technique
  - Progressive muscle relaxation
- Medication Tracker
- Therapy Session Tracker
- Exposure Hierarchy (for exposure therapy)
- Worry Time (scheduled worry period - CBT technique)
```

---

### 4.3 Severe Mental Illness Support (Schizophrenia, Bipolar Disorder)

**Prevalence:** ~2.8% of US adults (bipolar), ~1% (schizophrenia)
**Complexity:** Very High
**Pathway Duration:** Ongoing (lifelong)

#### Care Pathway Stages:

**Stage 1: Stabilization (Weeks 1-4)**
- Tasks:
  - Medication initiation/optimization (antipsychotic, mood stabilizer)
  - Safety assessment
  - Intensive case management
  - Family psychoeducation
  - Establish crisis plan
- Patient Education:
  - Medication adherence critical
  - Recognizing early warning signs of relapse
  - Avoiding substance use

**Stage 2: Maintenance (Ongoing)**
- Tasks:
  - Monthly psychiatry appointments
  - Long-acting injectable (LAI) medication if adherence issue
  - Supported employment/housing if needed
  - Family therapy
  - Lab monitoring (metabolic side effects of antipsychotics)
- Patient Education:
  - Recovery-oriented care
  - Relapse prevention
  - Side effect management

**Stage 3: Crisis Intervention (as needed)**
- Tasks:
  - Identify crisis (psychosis, mania, severe depression, suicidality)
  - Coordinate with crisis team
  - Medication adjustment
  - Possible hospitalization
- Patient Education:
  - Crisis resources

#### AI Features:

**ML Model: smi-relapse-v1**
- Input: Medication adherence (LAI records, pharmacy fills), appointment attendance, symptom check-ins, substance use
- Output: Relapse risk next 30 days
- Alerts: Missed appointments, medication non-adherence, early warning signs

**Proactive Outreach:**
- Trigger: Missed appointment → Automated call/SMS → If no response, outreach worker visit

**Frontend Widget: Recovery Dashboard** (simplified for cognitive impairment)
```
Components:
- Medication Reminders (large, simple)
- Next Appointment (prominent display)
- Crisis Contacts (one-touch call)
- Mood Tracker (simple emoji scale)
- Symptom Check-in (yes/no hallucinations, sleep, mood)
- Caregiver View (for family members):
  - Early warning sign checklist
  - Medication adherence
  - Appointment history
```

---

### 4.4 Dementia / Alzheimer's (Caregiver Support)

**Prevalence:** ~6.7 million Americans with Alzheimer's
**Complexity:** Very High
**Pathway Duration:** Ongoing (progressive disease)

#### Care Pathway Focus: Caregiver Support & Safety

**Stage 1: Diagnosis & Planning (Weeks 1-4)**
- Tasks:
  - Confirm diagnosis (cognitive testing, imaging)
  - Assess stage (mild, moderate, severe)
  - Legal planning (advance directives, power of attorney)
  - Caregiver education
  - Safety assessment (driving, falls, wandering)
- Caregiver Education:
  - Disease progression
  - Communication strategies
  - Behavioral management (agitation, sundowning)
  - Community resources

**Stage 2: Ongoing Management (Months 1-12+)**
- Tasks:
  - Cognitive enhancers if mild-moderate (donepezil, memantine)
  - Behavioral interventions (structured routines, redirection)
  - Caregiver respite planning
  - Fall prevention
  - Wandering prevention (GPS tracker, door alarms)
  - Advance care planning
- Caregiver Support:
  - Support groups
  - Respite care
  - Caregiver burden assessment (Zarit Burden Interview)

**Stage 3: Advanced Dementia (ongoing)**
- Tasks:
  - Comfort care
  - Hospice referral when appropriate
  - Manage complications (aspiration, infections)
- Caregiver Support:
  - End-of-life planning
  - Grief support

#### AI Features:

**ML Model: dementia-progression-v1**
- Input: Cognitive scores (MMSE, MoCA), functional status (ADLs), behavioral symptoms
- Output: Predicted time to next stage, care needs
- Action: Anticipatory guidance for caregivers

**Wandering Risk Detector:**
- Input: GPS data, door sensor data, time of day
- Output: Wandering event detection
- Action: Alert caregiver, trigger search if needed

**Frontend Widget: Dementia Care Dashboard** (For Caregiver)
```
Components:
- Daily Routine Checklist:
  - Morning routine (visual prompts)
  - Medication given
  - Meals
  - Activities
- Behavioral Log:
  - Agitation episodes (triggers, interventions)
  - Sleep disturbances
  - Wandering attempts
- Safety Checklist:
  - Doors/windows secured
  - Stove off
  - Medication locked
- GPS Tracker (if device worn)
- Caregiver Self-Care:
  - Respite hours this week
  - Caregiver burden score
  - Support group meetings
- Emergency Contacts
```

---

### 4.5 Migraine

**Prevalence:** ~39 million Americans
**Complexity:** Moderate
**Pathway Duration:** 6 months

#### Care Pathway Stages:

**Stage 1: Diagnosis & Trigger Identification (Weeks 1-4)**
- Tasks:
  - Headache diary (frequency, severity, duration, triggers)
  - Rule out secondary causes (neuroimaging if red flags)
  - MIDAS (Migraine Disability Assessment) score
  - Identify triggers (sleep, stress, foods, hormones, weather)
- Patient Education:
  - Migraine vs tension headache vs cluster headache
  - Trigger avoidance
  - When to seek emergency care (thunderclap headache, neurological deficits)

**Stage 2: Acute & Preventive Treatment (Weeks 5-12)**
- Tasks:
  - Acute treatment: Triptans, NSAIDs, antiemetics
  - Preventive treatment if ≥4 headache days/month (beta-blocker, amitriptyline, topiramate, CGRP inhibitor)
  - Headache diary continuation
  - Lifestyle modifications (regular sleep, exercise, hydration)
- Patient Education:
  - Medication overuse headache (avoid overuse of acute meds)
  - Preventive medication takes 6-8 weeks to work

**Stage 3: Optimization (Months 4-6)**
- Tasks:
  - Adjust preventive dose if needed
  - MIDAS reassessment
  - Biofeedback or cognitive-behavioral therapy if indicated
- Milestone: ≥50% reduction in headache days

#### AI Features:

**ML Model: migraine-forecast-v1**
- Input: Headache diary data, sleep, stress, menstrual cycle, weather data
- Output: Migraine probability next 24-48 hours
- Action: Pre-treatment suggestions, trigger avoidance reminders

**Trigger Clustering:**
- Input: Detailed headache + trigger logs
- Output: Statistical correlations (e.g., "80% of migraines preceded by poor sleep")
- Action: Personalized avoidance plan

**Frontend Widget: Migraine Diary & Insights**
```
Components:
- Headache Log:
  - Date, time of onset
  - Severity (1-10)
  - Duration
  - Location (unilateral, bilateral)
  - Aura (yes/no, type)
  - Associated symptoms (nausea, photophobia, phonophobia)
  - Triggers suspected
  - Medications taken, effectiveness
- MIDAS Score (quarterly)
- Trigger Insights:
  - Top 5 identified triggers
  - Trigger heatmap (calendar view)
- Medication Tracker:
  - Acute medication use (days/month - track for overuse)
  - Preventive medication adherence
- Menstrual Cycle Tracker (if applicable)
- Sleep & Stress Log
```

---

### 4.6 Epilepsy

**Prevalence:** ~3.4 million Americans
**Complexity:** High
**Pathway Duration:** Ongoing

#### Care Pathway Stages:

**Stage 1: Diagnosis & Classification (Weeks 1-4)**
- Tasks:
  - EEG (awake, sleep, possibly prolonged video-EEG)
  - MRI brain
  - Classify seizure type (focal, generalized)
  - Driving restrictions (state-specific, typically 3-12 months seizure-free)
  - Safety counseling (bathing, swimming, heights)
- Patient Education:
  - Seizure basics
  - Seizure first aid
  - Safety precautions

**Stage 2: Medication Management (Months 1-6)**
- Tasks:
  - Antiepileptic drug (AED) initiation (levetiracetam, lamotrigine, valproate, etc.)
  - Slow titration to minimize side effects
  - Seizure diary
  - Drug level monitoring if indicated
  - EEG repeat in 3-6 months
- Patient Education:
  - Medication adherence critical
  - Don't skip doses
  - Avoiding triggers (sleep deprivation, alcohol, flashing lights)

**Stage 3: Optimization (Months 7-12)**
- Tasks:
  - Seizure freedom goal
  - Adjust dose or add second AED if breakthrough seizures
  - Consider epilepsy surgery evaluation if drug-resistant (≥2 AEDs failed)
  - Women of childbearing age: contraception counseling (AED-drug interactions), folate supplementation
- Milestone: Seizure-free for 6-12 months (state-specific for driving)

#### AI Features:

**ML Model: seizure-trigger-v1**
- Input: Seizure diary, sleep log, medication adherence, menstrual cycle (if applicable)
- Output: Seizure risk probability next 24-48 hours
- Alerts: Missed AED doses, sleep deprivation

**Wearable Seizure Detection:** (if Empatica Embrace or similar)
- Input: Wrist-worn accelerometer, EDA (electrodermal activity)
- Output: Seizure detection
- Action: Alert caregiver, log event

**Frontend Widget: Epilepsy Management Dashboard**
```
Components:
- Seizure Diary:
  - Date, time
  - Type (focal, generalized, unknown)
  - Duration
  - Triggers identified
  - Post-ictal symptoms
  - Injuries sustained
- Medication Tracker (critical - missed doses = seizures)
- Sleep Log (sleep deprivation is major trigger)
- Trigger Log (alcohol, stress, illness, flashing lights)
- Seizure-Free Streak (motivating counter)
- Driving Eligibility Countdown (if applicable)
- Safety Plan (seizure first aid, emergency contacts)
- Wearable Data Integration (if available)
```

---

### 4.7 Parkinson's Disease

**Prevalence:** ~1 million Americans
**Complexity:** Very High
**Pathway Duration:** Ongoing (progressive)

#### Care Pathway Stages:

**Stage 1: Diagnosis & Baseline Assessment (Weeks 1-4)**
- Tasks:
  - Clinical diagnosis (bradykinesia + tremor/rigidity)
  - Assess stage (Hoehn & Yahr scale)
  - MDS-UPDRS (Movement Disorder Society - Unified Parkinson's Disease Rating Scale)
  - Rule out atypical parkinsonism (MSA, PSP, CBD)
  - Neurology/movement disorder specialist referral
- Patient Education:
  - PD basics (dopamine deficiency)
  - Slow progression
  - Symptoms fluctuate

**Stage 2: Medication Initiation (Months 1-6)**
- Tasks:
  - Levodopa/carbidopa initiation (most effective, but eventually motor fluctuations develop)
  - Dopamine agonists if young (delay levodopa complications)
  - Physical therapy, occupational therapy
  - Speech therapy if dysarthria
  - Assess for non-motor symptoms (constipation, depression, sleep disorders, orthostatic hypotension)
- Patient Education:
  - Medication timing critical
  - Exercise is disease-modifying (most important non-drug intervention)

**Stage 3: Ongoing Management (Months 7+)**
- Tasks:
  - Adjust medications as disease progresses
  - Manage motor fluctuations (wearing off, dyskinesias)
  - Treat non-motor symptoms
  - Consider advanced therapies if medication optimization insufficient:
    - Deep brain stimulation (DBS)
    - Duopa (intestinal levodopa pump)
    - Apomorphine pump
  - Fall prevention
  - Swallowing assessment (aspiration risk)
- Milestone: Maintain function, minimize off time

#### AI Features:

**ML Model: pd-progression-v1**
- Input: UPDRS scores over time, medication regimen, motor fluctuations
- Output: Predicted progression rate, time to advanced therapy consideration
- Alerts: Fall risk, dyskinesia worsening

**Medication Timing Optimizer:**
- Input: Symptom logs (on time, off time, dyskinesia time), medication schedule
- Output: Optimized dosing schedule
- Action: Recommendations to neurologist

**Frontend Widget: Parkinson's Tracker**
```
Components:
- Motor Symptom Log:
  - Tremor severity (0-4)
  - Rigidity
  - Bradykinesia
  - Postural instability
  - On/Off/Dyskinesia time (hours per day)
- Medication Schedule (strict timing required)
- Exercise Log (critical - recommend 2.5 hrs/week minimum)
- Fall Log (date, injury, circumstance)
- Non-Motor Symptom Tracker:
  - Constipation
  - Mood (depression, anxiety)
  - Sleep quality
  - Orthostatic dizziness
- UPDRS Score (quarterly)
- Speech/Swallowing Issues
```

---

## 🎗️ Category 5: Cancer & Treatment Coordination (2 Conditions)

### 5.1 Cancer Care Coordination (Active Treatment)

**Complexity:** Very High
**Pathway Duration:** Varies (6-24 months depending on cancer type/stage)

#### Care Pathway Stages:

**Stage 1: Diagnosis & Treatment Planning (Weeks 1-4)**
- Tasks:
  - Confirm diagnosis (pathology, staging imaging)
  - TNM staging
  - Multidisciplinary tumor board
  - Treatment plan (surgery, chemotherapy, radiation, immunotherapy, targeted therapy)
  - Genetic testing/counseling if indicated
  - Fertility preservation discussion (if applicable)
  - Oncology nurse navigator assignment
- Patient Education:
  - Cancer type/stage explanation
  - Treatment options, goals (curative vs palliative)
  - Side effect expectations

**Stage 2: Active Treatment (Months 1-6+)**
- Tasks:
  - Chemotherapy cycles (schedule, lab monitoring)
  - Radiation therapy (daily, weeks)
  - Surgery (pre-op, post-op care)
  - Side effect management:
    - Nausea/vomiting (antiemetics)
    - Neutropenic fever (G-CSF, antibiotics)
    - Mucositis, diarrhea
    - Neuropathy
    - Fatigue
  - Symptom assessment (CTCAE grading)
  - Nutritional support
  - Psychosocial support (social work, psychology)
  - Palliative care integration (for symptom management, not just end-of-life)
- Patient Education:
  - When to call oncology (fever, severe symptoms)
  - Infection prevention (neutropenia)
  - Nutrition during treatment

**Stage 3: Response Assessment & Completion (Variable)**
- Tasks:
  - Imaging to assess response (CT, PET, MRI)
  - Determine next steps (surgery if chemo first, more chemo, radiation, surveillance)
  - Survivorship care plan
- Milestone: Treatment completion

#### AI Features:

**ML Model: chemo-toxicity-v1**
- Input: Chemotherapy regimen, patient age, comorbidities, baseline labs
- Output: Risk of severe toxicity (neutropenic fever, neuropathy, cardiotoxicity)
- Action: Dose adjustments, prophylactic G-CSF, closer monitoring

**Symptom Red-Flag Detector:**
- Input: Daily symptom check-ins (CTCAE patient-reported outcomes)
- Output: Triage level (self-manage, call oncology, emergency department)
- Alerts: Fever + neutropenia, severe dehydration, chest pain

**Frontend Widget: Cancer Treatment Dashboard**
```
Components:
- Treatment Timeline:
  - Past treatments (dates, cycles)
  - Upcoming treatments (next chemo date, radiation sessions remaining)
- Symptom Check-in (daily PRO-CTCAE):
  - Nausea, vomiting (severity, frequency)
  - Diarrhea, constipation
  - Fatigue
  - Pain
  - Neuropathy (numbness, tingling)
  - Shortness of breath
- Lab Results (WBC, ANC, platelets, hemoglobin)
- Side Effect Management:
  - Medications prescribed (antiemetics, pain meds)
  - Non-pharmacologic strategies
- Nutrition Tracker (calorie intake, weight)
- Direct Messaging to Oncology Team
- Emergency Instructions:
  - Call if fever >100.4°F
  - Call if severe symptoms
  - ER for chest pain, difficulty breathing
- Appointments (oncology, radiation, labs)
```

---

### 5.2 Post-Treatment Survivorship

**Complexity:** Moderate-High
**Pathway Duration:** 5 years (intensive), then ongoing

#### Care Pathway Stages:

**Stage 1: Treatment Completion & Survivorship Care Plan (Weeks 1-4)**
- Tasks:
  - Survivorship care plan (summary of treatment, surveillance schedule)
  - Transition to PCP or survivorship clinic
  - Assess for late effects (fatigue, cognitive changes, neuropathy)
  - Psychosocial assessment (fear of recurrence, PTSD)
  - Lifestyle counseling (nutrition, exercise, smoking cessation)
- Patient Education:
  - Follow-up schedule
  - Signs of recurrence
  - Late effects to monitor

**Stage 2: Surveillance (Years 1-5)**
- Tasks (schedule varies by cancer type):
  - Physical exams (every 3-6 months)
  - Imaging (CT, MRI, PET) as indicated
  - Tumor markers (if applicable)
  - Mammography (breast cancer)
  - Colonoscopy (colon cancer)
  - Screen for late effects:
    - Cardiac (if anthracyclines or chest radiation)
    - Pulmonary (if chest radiation)
    - Secondary cancers
  - Manage chronic symptoms (neuropathy, lymphedema, fatigue)
- Patient Education:
  - Healthy lifestyle reduces recurrence
  - Self-exams (breast, testicular)

**Stage 3: Long-Term Follow-Up (5+ years)**
- Tasks:
  - Annual exams
  - Age-appropriate cancer screenings
  - Late effects monitoring
- Milestone: 5 years disease-free (often considered "cured" for many cancers)

#### AI Features:

**ML Model: recurrence-risk-v1**
- Input: Cancer type, stage, treatment received, biomarkers, genetic mutations
- Output: 5-year recurrence risk
- Action: Intensify surveillance if high risk

**Frontend Widget: Survivorship Dashboard**
```
Components:
- Surveillance Calendar (upcoming scans, exams)
- Symptom Tracker (watch for recurrence symptoms)
- Late Effect Monitoring:
  - Cardiac function (if at risk)
  - Bone density (if hormonal therapy)
  - Secondary cancer screening
- Healthy Lifestyle Tracker:
  - BMI (maintain healthy weight)
  - Exercise (150 min/week reduces recurrence)
  - Smoking status
- Fear of Recurrence Scale (FCRI)
- Survivorship Care Plan (always accessible PDF)
```

---

## 🦴 Category 6: Musculoskeletal & Chronic Pain (3 Conditions)

### 6.1 Osteoarthritis (OA)

**Prevalence:** ~32.5 million US adults
**Complexity:** Moderate
**Pathway Duration:** Ongoing (chronic, progressive)

#### Care Pathway Stages:

**Stage 1: Diagnosis & Non-Pharmacologic Management (Weeks 1-4)**
- Tasks:
  - Clinical diagnosis (joint pain, stiffness, X-ray if indicated)
  - Assess severity (WOMAC score for knee/hip OA)
  - Identify modifiable risk factors (obesity, joint injury)
  - Physical therapy referral
  - Occupational therapy (assistive devices, joint protection)
  - Weight loss counseling if BMI >25
- Patient Education:
  - OA basics (cartilage degeneration)
  - Exercise paradox (hurts but helps)
  - Weight loss importance (every 1 lb lost = 4 lbs off knees)

**Stage 2: Pharmacologic & Interventional Management (Months 1-6)**
- Tasks:
  - Acetaminophen or NSAIDs (topical preferred to minimize systemic side effects)
  - Intra-articular corticosteroid injections if severe flare
  - Hyaluronic acid injections (controversial efficacy)
  - Duloxetine if chronic pain with central sensitization
  - Continued PT, weight loss
- Patient Education:
  - NSAIDs have GI/cardiac risks (use lowest dose, shortest duration)
  - Glucosamine/chondroitin (limited evidence, but low risk)

**Stage 3: Advanced Management (if conservative fails)**
- Tasks:
  - Joint replacement evaluation (knee, hip arthroplasty)
  - Pre-surgical optimization (weight loss, smoking cessation, diabetes control)
- Milestone: Improved function, reduced pain

#### AI Features:

**ML Model: oa-progression-v1**
- Input: X-ray severity, BMI, activity level, pain scores
- Output: Predicted time to joint replacement need
- Action: Motivate weight loss, PT adherence

**Flare Predictor:**
- Input: Activity logs, weather data (barometric pressure changes), pain scores
- Output: Flare risk next 48 hours
- Action: Pre-treatment (ice, rest, NSAIDs)

**Frontend Widget: OA Management Dashboard**
```
Components:
- Pain & Function Tracker:
  - WOMAC score (monthly)
  - Pain severity (0-10)
  - Functional limitations (stairs, walking distance)
- Weight Tracker (if overweight/obese)
- Exercise Log (PT exercises, low-impact cardio)
- Medication Tracker (NSAIDs use - track for overuse)
- Flare Log (date, trigger, duration, treatment)
- Joint Replacement Readiness Assessment (if applicable)
```

---

### 6.2 Lower Back Pain / Persistent Pain

**Prevalence:** ~20% of US adults
**Complexity:** Moderate-High
**Pathway Duration:** 3-6 months

#### Care Pathway Stages:

**Stage 1: Triage & Red Flag Assessment (Week 1)**
- Tasks:
  - Assess for red flags (cauda equina, cancer, infection, fracture)
  - If red flags → Urgent imaging (MRI)
  - If no red flags → Reassurance, early mobilization
  - Self-management education
- Patient Education:
  - Most low back pain resolves in 4-6 weeks
  - Stay active (bed rest delays recovery)
  - Red flags to watch for (bowel/bladder changes, weakness, fever)

**Stage 2: Conservative Management (Weeks 2-12)**
- Tasks:
  - Physical therapy (core strengthening, flexibility)
  - NSAIDs or acetaminophen (short-term)
  - Heat/ice
  - Avoid opioids unless severe and acute
  - Consider muscle relaxants short-term if spasm
  - If no improvement by 6 weeks → Consider imaging (MRI)
- Patient Education:
  - Proper lifting mechanics
  - Posture
  - Ergonomics

**Stage 3: Interventional Management (if persistent >12 weeks)**
- Tasks:
  - Epidural steroid injection if radiculopathy
  - Consider referral to pain clinic
  - Cognitive-behavioral therapy for chronic pain
  - Consider alternative therapies (acupuncture, chiropractic, massage)
- Patient Education:
  - Chronic pain management (biopsychosocial model)
  - Pacing, acceptance strategies

#### AI Features:

**ML Model: back-pain-chronicity-v1**
- Input: Pain severity, duration, psychological factors (anxiety, catastrophizing), prior episodes
- Output: Risk of chronic pain (>3 months)
- Action: Early psychological intervention if high risk

**Frontend Widget: Back Pain Tracker**
```
Components:
- Pain Severity & Location (0-10, body map)
- Functional Status (Oswestry Disability Index)
- Red Flag Checklist (always visible)
- Activity Tracker (staying active is key)
- Medication Log (NSAIDs, avoid long-term opioids)
- PT Exercise Adherence
```

---

### 6.3 Osteoporosis (Fracture Risk Prevention)

**Prevalence:** ~10 million US adults with osteoporosis
**Complexity:** Moderate
**Pathway Duration:** Ongoing

#### Care Pathway Stages:

**Stage 1: Screening & Diagnosis (Weeks 1-2)**
- Tasks:
  - DEXA scan (bone mineral density)
  - T-score interpretation (Normal >-1, Osteopenia -1 to -2.5, Osteoporosis <-2.5)
  - FRAX score (10-year fracture risk)
  - Assess for secondary causes (vitamin D deficiency, hyperparathyroidism, celiac, etc.)
  - Labs: Calcium, vitamin D, PTH, TSH
- Patient Education:
  - Osteoporosis basics (silent disease until fracture)
  - Fall prevention

**Stage 2: Treatment (Ongoing)**
- Tasks:
  - Calcium 1200 mg/day (diet + supplement)
  - Vitamin D 800-1000 IU/day
  - Bisphosphonate (alendronate, risedronate) if osteoporosis or high fracture risk
  - Weight-bearing exercise
  - Fall risk assessment (home safety, vision, balance)
  - Repeat DEXA in 2 years
- Patient Education:
  - Medication adherence (bisphosphonates must be taken correctly - fasting, upright 30 min)
  - Fall prevention (remove trip hazards, vision check, avoid sedating meds)

#### AI Features:

**ML Model: fracture-risk-v1**
- Input: FRAX score + additional factors (frailty, medications that increase falls)
- Output: Enhanced fracture risk
- Action: Intensify treatment, fall prevention

**Frontend Widget: Bone Health Dashboard**
```
Components:
- DEXA Results (T-score over time)
- FRAX Score
- Calcium & Vitamin D Tracker
- Medication Adherence (bisphosphonate)
- Exercise Log (weight-bearing, balance exercises)
- Fall Risk Assessment
- Home Safety Checklist
```

---

## 🩺 Category 7: Digestive & Autoimmune (2 Conditions)

### 7.1 IBS (Irritable Bowel Syndrome)

**Prevalence:** ~10-15% of US adults
**Complexity:** Moderate
**Pathway Duration:** 6 months

#### Care Pathway (Brief):

**Stages:**
1. Diagnosis (Rome criteria), rule out IBD
2. Dietary intervention (low FODMAP diet)
3. Medication (antispasmodics, laxatives/antidiarrheals, gut-brain meds)
4. Psychological therapy (CBT, gut-directed hypnotherapy)

**AI Features:**
- Trigger clustering (food diary analysis)
- Symptom pattern recognition

**Frontend Widget:** Symptom Diary, Food Log, Trigger Insights

---

### 7.2 Inflammatory Bowel Disease (Crohn's / Ulcerative Colitis)

**Prevalence:** ~3 million US adults
**Complexity:** High
**Pathway Duration:** Ongoing

#### Care Pathway (Brief):

**Stages:**
1. Diagnosis (colonoscopy, imaging)
2. Induction therapy (steroids, biologics)
3. Maintenance therapy (immunosuppressants, biologics)
4. Flare management
5. Surgery consideration (if refractory)

**AI Features:**
- Flare prediction (symptoms + biomarkers)
- Biologic response prediction

**Frontend Widget:** Symptom Tracker (stool frequency, blood, pain), Medication Adherence, CRP/Calprotectin Trends

---

## 🩺 Category 8: Sexual Health / Infections (1 Condition)

### 8.1 UTI (Urinary Tract Infection) & Recurrent Infections

**Prevalence:** ~8 million UTI visits/year
**Complexity:** Low-Moderate
**Pathway Duration:** 1-2 weeks (acute), 3-6 months (recurrent)

#### Care Pathway (Brief):

**Stages:**
1. Diagnosis (symptoms, urinalysis, culture)
2. Antibiotic treatment (3-7 days)
3. If recurrent (≥2 in 6 months or ≥3 in 1 year):
   - Identify triggers (sexual activity, spermicides)
   - Prophylactic strategies (post-coital antibiotic, daily low-dose antibiotic, cranberry, D-mannose)
   - Imaging to rule out structural abnormalities

**AI Features:**
- Recurrence risk prediction
- Antibiotic resistance pattern analysis (local antibiogram integration)

**Frontend Widget:** Symptom Checker (dysuria, frequency, urgency), Treatment Log, Prevention Checklist

---

## 📊 Cross-Cutting Features (All 30 Illness Areas)

### 1. Unified Patient Portal

**Home Dashboard:**
```
- Active Pathways (cards for each condition)
- Today's Tasks (check-ins, medications, appointments)
- Recent Alerts (abnormal vitals, overdue tasks)
- Messages (care team)
- Health Summary (vitals, labs, medications)
```

### 2. Clinician Cohort Management

**All-Patients View:**
```
- Filter by:
  - Condition type
  - Risk level
  - Control status
  - Overdue tasks
  - Pathway stage
- Sort by:
  - Risk score
  - Last contact
  - Next due task
- Bulk actions:
  - Message cohort
  - Assign tasks
  - Generate reports
```

### 3. Real-Time Alerting System

**Alert Types:**
- CRITICAL (immediate clinician notification): Suicidal ideation, chest pain, severe deterioration
- URGENT (review within 24 hours): High risk scores, abnormal labs, missed critical medications
- ROUTINE (review within 7 days): Overdue check-ins, mild non-adherence

**Alert Routing:**
- Alerts routed based on role (physician, nurse, pharmacist, care coordinator)
- Escalation if not acknowledged

### 4. Educational Content Library

**Content Types:**
- Videos (condition basics, medication how-tos)
- Interactive modules (quizzes, decision aids)
- PDFs (handouts, action plans)
- Links to vetted external resources

**Content Delivery:**
- Pushed based on pathway stage
- Accessible on-demand
- Track completion

### 5. Multi-Channel Communication

**Channels:**
- In-app messaging (secure, HIPAA-compliant)
- SMS (reminders, alerts)
- Email (summaries, reports)
- Push notifications (mobile app)
- Voice calls (automated for check-ins)

---

## 🎯 Phase 4 Success Metrics

### Clinical Metrics:
- ✅ 85%+ patients enrolled in appropriate pathways
- ✅ 80%+ pathway task completion rate
- ✅ 70%+ symptom check-in completion rate
- ✅ 50%+ reduction in emergency department visits for chronic conditions
- ✅ 30%+ improvement in condition-specific quality metrics (HbA1c control, BP control, asthma control, depression remission)

### Technical Metrics:
- ✅ All 30 ML models deployed and operational
- ✅ Model inference latency <200ms p99
- ✅ Real-time alert delivery <5 seconds
- ✅ Frontend widget load time <1 second
- ✅ Mobile app crash rate <1%

### Engagement Metrics:
- ✅ Daily active users: 40%+ of enrolled patients
- ✅ Weekly active users: 70%+ of enrolled patients
- ✅ Average session duration: >3 minutes
- ✅ Patient satisfaction score: >4.0/5.0

---

## 📁 Deliverables

### Care Pathway Templates:
- 30 pathway templates in JSONB format (stored in pathway_templates table)
- Stage definitions, task definitions, milestone criteria

### ML Models:
- 30+ models trained and deployed to TensorFlow Serving
- Model cards documenting performance, limitations, ethical considerations

### Frontend Components:
- 30 condition-specific dashboard widgets (React components)
- Reusable charting library (vitals, labs, symptoms)
- Unified patient portal (React app)
- Clinician cohort view (React app)

### Database Extensions:
- 30+ new tables for condition-specific data
- Indexes for performance
- Triggers for real-time calculations

### Documentation:
- Clinical pathway guides (for each of 30 conditions)
- Patient education materials (30 condition libraries)
- Clinician training materials
- API documentation

---

## 🚀 Next Phase Preview

**Phase 5 (Weeks 24-30): Frontend Copilots**

Will implement:
- 4 role-based AI copilots (Patient, Clinician, Finance, Pharmacy)
- RAG-based chat interface with medical knowledge base
- Voice input/output
- Proactive AI assistance (suggestions, insights)
- Multi-modal interaction (text, voice, visual)

---

**Document Version:** 1.0
**Last Updated:** 2026-01-08
**Status:** ✅ Ready for Implementation
**Dependencies:** Phase 1, 2, 3 Complete
**Estimated Duration:** 6 weeks (Weeks 18-24)
