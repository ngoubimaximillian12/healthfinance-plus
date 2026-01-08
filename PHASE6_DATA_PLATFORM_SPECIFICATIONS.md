# Phase 6 Implementation Specifications: Data Platform Architecture

## Overview

**Timeline:** Weeks 30-36
**Focus:** Data Lakehouse, Real-Time CDC, Analytics, ML Feature Store
**Deliverables:** Complete data platform with bronze/silver/gold layers, BI dashboards, Patient 360

---

## 📋 Objectives

### Primary Goals:
1. Deploy complete data lakehouse (MinIO S3 + Apache Iceberg)
2. Implement real-time CDC pipeline (Debezium → Kafka → Lake)
3. Build bronze/silver/gold data layers
4. Deploy BI tool (Metabase or Apache Superset)
5. Implement ML feature store (Feast)
6. Create Patient 360 unified view
7. Establish data quality framework

### Success Criteria:
- ✅ All 27 services streaming changes to data lake via CDC
- ✅ CDC latency <5 seconds from database to lake
- ✅ BI dashboard query latency <3 seconds
- ✅ Data quality score >95%
- ✅ Patient 360 view available for all patients
- ✅ Feature store serving features with <50ms latency

---

## 🏗️ Data Architecture Overview

```
Production Databases (PostgreSQL)
       ↓
   Debezium CDC Connectors
       ↓
   Kafka Topics (Change Streams)
       ↓
   ┌─────────────────────────────────────┐
   │         Data Lake (MinIO S3)         │
   ├─────────────────────────────────────┤
   │  Bronze Layer (Raw CDC data)         │
   │  ↓                                   │
   │  Silver Layer (Cleaned & Validated)  │
   │  ↓                                   │
   │  Gold Layer (Business Aggregates)    │
   └─────────────────────────────────────┘
       ↓                    ↓              ↓
   BI Dashboards      ML Training    Feature Store
   (Metabase)         (Python)       (Feast)
```

---

## 💾 Data Lakehouse Implementation

### Technology Stack:

**Object Storage:**
- MinIO (S3-compatible) for development/on-prem
- AWS S3 for production cloud deployment
- Bucket structure: `s3://healthfinance-datalake/`

**Table Format:**
- Apache Iceberg (supports schema evolution, time travel, ACID transactions)
- Parquet file format (columnar, compressed)

**Query Engine:**
- Apache Spark for batch processing
- Trino/Presto for interactive queries
- Iceberg REST catalog for metadata

### Data Lake Structure:

```
s3://healthfinance-datalake/
├── bronze/                    # Raw data (CDC events)
│   ├── users/
│   │   ├── 2026/01/08/hour=00/
│   │   │   └── data-00001.parquet
│   │   └── 2026/01/08/hour=01/
│   │       └── data-00001.parquet
│   ├── appointments/
│   ├── medical_records/
│   ├── prescriptions/
│   ├── insurance_policies/
│   ├── billing_invoices/
│   ├── notifications/
│   ├── lab_results/
│   ├── pharmacy_inventory/
│   ├── care_pathways/
│   ├── risk_scores/
│   ├── decisions/
│   ├── review_tasks/
│   ├── providers/
│   ├── engagement_scores/
│   └── ... (all 27 service databases)
│
├── silver/                   # Cleaned, validated, deduplicated
│   ├── patient_timeline/
│   │   └── patient_id=UUID/
│   │       └── data.parquet
│   ├── clinical_features/
│   ├── billing_aggregates/
│   ├── medication_history/
│   ├── vitals_normalized/
│   ├── appointments_enriched/
│   └── ...
│
└── gold/                    # Business-level aggregates
    ├── patient_360/         # Complete patient view
    ├── provider_metrics/     # Provider performance
    ├── operational_kpis/    # Dashboards
    ├── ml_training_datasets/ # Prepared datasets for ML
    └── population_health/   # De-identified aggregates
```

---

## 🔄 CDC Pipeline (Debezium)

### Architecture:

```
PostgreSQL DB → Debezium Connector → Kafka Topic → Kafka Connect Sink → MinIO S3 (Bronze)
```

### Debezium Connector Configuration:

**Example: Medical Records Service**

```json
{
  "name": "medical-records-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "database.hostname": "postgres",
    "database.port": "5433",
    "database.user": "healthfinance",
    "database.password": "healthfinance123",
    "database.dbname": "healthfinance_medical_records",
    "database.server.name": "medical-records-db",
    "table.include.list": "public.medical_records,public.medical_record_events,public.vitals,public.diagnoses",
    "plugin.name": "pgoutput",
    "publication.name": "medical_records_pub",
    "slot.name": "medical_records_slot",
    "snapshot.mode": "initial",
    "time.precision.mode": "adaptive_time_microseconds",
    "decimal.handling.mode": "precise",
    "tombstones.on.delete": "true",
    "transforms": "unwrap,addPrefix",
    "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState",
    "transforms.unwrap.drop.tombstones": "false",
    "transforms.addPrefix.type": "org.apache.kafka.connect.transforms.RegexRouter",
    "transforms.addPrefix.regex": ".*",
    "transforms.addPrefix.replacement": "cdc.medical_records.$0"
  }
}
```

**Deploy Connectors for All 27 Services:**
- user-service → cdc.users.*
- appointment-service → cdc.appointments.*
- medical-records-service → cdc.medical_records.*
- prescription-service → cdc.prescriptions.*
- insurance-service → cdc.insurance.*
- billing-service → cdc.billing.*
- notification-service → cdc.notifications.*
- laboratory-service → cdc.laboratory.*
- pharmacy-service → cdc.pharmacy.*
- intelligence-hub-service → cdc.decisions.*
- risk-scoring-service → cdc.risk_scores.*
- care-pathway-service → cdc.care_pathways.*
- triage-service → cdc.triage.*
- provider-service → cdc.providers.*
- patient-engagement-service → cdc.engagement.*
- pricing-service → cdc.pricing.*
- fraud-detection-service → cdc.fraud.*
- claims-orchestrator-service → cdc.claims.*
- document-ai-service → cdc.documents.*
- (+ 8 more services)

### Kafka Connect S3 Sink Configuration:

```json
{
  "name": "s3-sink-bronze",
  "config": {
    "connector.class": "io.confluent.connect.s3.S3SinkConnector",
    "tasks.max": "10",
    "topics.regex": "cdc\\..*",
    "s3.region": "us-east-1",
    "s3.bucket.name": "healthfinance-datalake",
    "s3.part.size": "5242880",
    "flush.size": "1000",
    "rotate.interval.ms": "300000",
    "storage.class": "io.confluent.connect.s3.storage.S3Storage",
    "format.class": "io.confluent.connect.s3.format.parquet.ParquetFormat",
    "parquet.codec": "snappy",
    "partitioner.class": "io.confluent.connect.storage.partitioner.TimeBasedPartitioner",
    "path.format": "'bronze/'YYYY'/'MM'/'dd'/hour='HH",
    "partition.duration.ms": "3600000",
    "timestamp.extractor": "Record",
    "schema.compatibility": "NONE"
  }
}
```

---

## 🔵 Bronze Layer (Raw Data)

### Purpose:
Store raw CDC events as-is, immutable audit trail.

### Characteristics:
- All changes captured (inserts, updates, deletes)
- Original schema preserved
- Partitioned by date/hour
- Parquet format (snappy compression)
- Iceberg table for ACID guarantees

### Iceberg Table Creation:

```sql
CREATE TABLE bronze.medical_records (
    before STRUCT<...>,         -- Record state before change
    after STRUCT<...>,          -- Record state after change
    source STRUCT<...>,         -- Metadata (db, table, timestamp, etc.)
    op STRING,                  -- Operation type (c=create, u=update, d=delete, r=read)
    ts_ms BIGINT,              -- Event timestamp (milliseconds)
    transaction STRUCT<...>     -- Transaction metadata
)
USING iceberg
PARTITIONED BY (days(ts_ms))
LOCATION 's3://healthfinance-datalake/bronze/medical_records'
TBLPROPERTIES (
    'write.format.default' = 'parquet',
    'write.parquet.compression-codec' = 'snappy'
);
```

### Data Retention:
- Bronze layer: 5 years
- Older data archived to cheaper storage (S3 Glacier)

---

## 🥈 Silver Layer (Cleaned & Validated)

### Purpose:
Cleaned, deduplicated, validated data ready for analysis.

### Transformations:

**1. Deduplication:**
- Remove duplicate CDC events (can occur with Kafka retries)
- Keep latest version per primary key

**2. Schema Normalization:**
- Flatten nested structures
- Standardize data types
- Handle NULLs consistently

**3. Data Validation:**
- Range checks (e.g., age 0-120, BP 70-250/40-150)
- Referential integrity (e.g., patient_id exists in users table)
- Business rule validation (e.g., appointment_date not in past)

**4. PII Handling:**
- Mask/encrypt sensitive fields for analytics
- Create de-identified datasets

**5. Enrichment:**
- Add derived columns (e.g., age from DOB, BMI from height/weight)
- Join with reference data (e.g., zip code → geographic region)

### Apache Spark Jobs:

**Example: Bronze → Silver for Medical Records**

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *
from pyspark.sql.types import *

spark = SparkSession.builder \
    .appName("Bronze to Silver - Medical Records") \
    .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions") \
    .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkSessionCatalog") \
    .config("spark.sql.catalog.spark_catalog.type", "hive") \
    .getOrCreate()

# Read bronze data (last 1 hour)
bronze_df = spark.read.format("iceberg") \
    .load("s3://healthfinance-datalake/bronze.medical_records") \
    .filter(col("ts_ms") > unix_timestamp(current_timestamp()) * 1000 - 3600000)

# Extract 'after' state (current record)
silver_df = bronze_df.select("after.*", "op", "ts_ms")

# Deduplication (keep latest)
from pyspark.sql.window import Window
window_spec = Window.partitionBy("record_id").orderBy(desc("ts_ms"))
silver_df = silver_df.withColumn("row_num", row_number().over(window_spec)) \
    .filter(col("row_num") == 1) \
    .drop("row_num")

# Data Validation
silver_df = silver_df.filter(
    (col("patient_id").isNotNull()) &
    (col("record_date").isNotNull()) &
    (col("record_date") <= current_date())
)

# Enrichment: Calculate age
silver_df = silver_df.withColumn(
    "patient_age",
    floor(datediff(current_date(), col("patient_dob")) / 365.25)
)

# Mask PII for analytics (optional view)
silver_df_masked = silver_df \
    .withColumn("patient_name", lit("***MASKED***")) \
    .withColumn("ssn", lit(None))

# Write to Silver layer (Iceberg)
silver_df.write.format("iceberg") \
    .mode("append") \
    .save("s3://healthfinance-datalake/silver.medical_records")

# Data Quality Checks
quality_metrics = {
    "total_records": silver_df.count(),
    "null_patient_id": silver_df.filter(col("patient_id").isNull()).count(),
    "future_dates": silver_df.filter(col("record_date") > current_date()).count(),
    "invalid_ages": silver_df.filter((col("patient_age") < 0) | (col("patient_age") > 120)).count()
}

# Log quality metrics
print(f"Data Quality Metrics: {quality_metrics}")
# Optionally alert if quality thresholds not met
```

**Schedule:** Spark jobs run every hour (batch processing)

### Silver Tables:

```sql
-- Patient Timeline (all events for a patient)
CREATE TABLE silver.patient_timeline (
    patient_id UUID,
    event_timestamp TIMESTAMP,
    event_type STRING,       -- APPOINTMENT, LAB_RESULT, PRESCRIPTION, DIAGNOSIS, etc.
    event_source STRING,     -- Table name
    event_data STRUCT<...>,  -- Relevant fields
    ...
)
USING iceberg
PARTITIONED BY (patient_id, days(event_timestamp));

-- Vitals Normalized
CREATE TABLE silver.vitals_normalized (
    vital_id UUID,
    patient_id UUID,
    measurement_timestamp TIMESTAMP,
    vital_type STRING,        -- BP, HR, TEMP, WEIGHT, etc.
    value DECIMAL(10,2),
    unit STRING,
    source STRING,            -- CLINIC, HOME, WEARABLE
    ...
)
USING iceberg
PARTITIONED BY (vital_type, days(measurement_timestamp));

-- Appointments Enriched
CREATE TABLE silver.appointments_enriched (
    appointment_id UUID,
    patient_id UUID,
    provider_id UUID,
    appointment_datetime TIMESTAMP,
    status STRING,
    type STRING,
    duration_minutes INT,
    -- Enriched fields
    patient_age INT,
    patient_conditions ARRAY<STRING>,
    provider_specialty STRING,
    distance_miles DECIMAL(5,2),
    ...
)
USING iceberg
PARTITIONED BY (days(appointment_datetime));
```

---

## 🥇 Gold Layer (Business Aggregates)

### Purpose:
Pre-aggregated, business-ready datasets for dashboards, reports, and ML training.

### Gold Tables:

#### 1. Patient 360 (Complete Patient View)

**Purpose:** Single source of truth for all patient data

```sql
CREATE TABLE gold.patient_360 (
    patient_id UUID PRIMARY KEY,

    -- Demographics
    first_name STRING,
    last_name STRING,
    date_of_birth DATE,
    age INT,
    gender STRING,
    race STRING,
    ethnicity STRING,
    address STRUCT<...>,
    phone STRING,
    email STRING,

    -- Active Conditions
    active_conditions ARRAY<STRUCT<
        condition STRING,
        icd10_code STRING,
        diagnosed_date DATE,
        severity STRING
    >>,

    -- Current Medications
    current_medications ARRAY<STRUCT<
        medication STRING,
        dosage STRING,
        frequency STRING,
        start_date DATE,
        prescriber STRING,
        adherence_rate DECIMAL(3,2)
    >>,

    -- Allergies
    allergies ARRAY<STRING>,

    -- Recent Vitals (last 30 days)
    recent_vitals STRUCT<
        bp_systolic_avg INT,
        bp_diastolic_avg INT,
        heart_rate_avg INT,
        weight_lbs DECIMAL(5,2),
        bmi DECIMAL(4,2),
        last_measurement_date DATE
    >,

    -- Recent Labs (last 90 days)
    recent_labs STRUCT<
        hba1c DECIMAL(3,1),
        ldl_cholesterol INT,
        egfr INT,
        last_lab_date DATE
    >,

    -- Care Pathways
    active_pathways ARRAY<STRUCT<
        pathway_name STRING,
        current_stage STRING,
        progress_score DECIMAL(3,2),
        initiated_date DATE,
        expected_completion DATE
    >>,

    -- Risk Scores
    risk_scores STRUCT<
        readmission_30day DECIMAL(3,2),
        emergency_visit_90day DECIMAL(3,2),
        medication_adherence DECIMAL(3,2),
        overall_health_risk STRING
    >>,

    -- Engagement
    engagement_score DECIMAL(3,2),
    last_portal_login TIMESTAMP,
    last_appointment TIMESTAMP,
    next_appointment TIMESTAMP,

    -- Insurance
    insurance_payer STRING,
    policy_number STRING,
    coverage_status STRING,

    -- Provider Relationships
    primary_care_provider STRING,
    specialists ARRAY<STRUCT<
        specialty STRING,
        provider_name STRING
    >>,

    -- Utilization (last 12 months)
    utilization_last_12m STRUCT<
        office_visits INT,
        emergency_visits INT,
        hospitalizations INT,
        total_cost DECIMAL(10,2)
    >>,

    -- Meta
    record_last_updated TIMESTAMP
)
USING iceberg
LOCATION 's3://healthfinance-datalake/gold/patient_360';
```

**Update Frequency:** Daily batch job (aggregates data from silver layer)

#### 2. Provider Metrics

```sql
CREATE TABLE gold.provider_metrics (
    provider_id UUID,
    metric_date DATE,

    -- Volume
    patients_seen INT,
    new_patients INT,
    appointments_scheduled INT,
    appointments_completed INT,
    no_show_rate DECIMAL(3,2),

    -- Quality
    patient_satisfaction_avg DECIMAL(2,1),   -- 1-5 scale
    review_count INT,
    chronic_disease_control_rate DECIMAL(3,2),
    preventive_care_completion_rate DECIMAL(3,2),

    -- Efficiency
    avg_wait_time_minutes INT,
    avg_visit_duration_minutes INT,
    same_day_appointment_availability DECIMAL(3,2),

    -- Outcomes
    readmission_rate DECIMAL(3,2),
    emergency_visit_rate DECIMAL(3,2),

    -- Financial
    total_charges DECIMAL(10,2),
    insurance_approval_rate DECIMAL(3,2),

    PRIMARY KEY (provider_id, metric_date)
)
USING iceberg
PARTITIONED BY (metric_date);
```

#### 3. Operational KPIs

```sql
CREATE TABLE gold.operational_kpis (
    kpi_date DATE PRIMARY KEY,

    -- Patient Engagement
    daily_active_users INT,
    weekly_active_users INT,
    monthly_active_users INT,
    avg_session_duration_minutes INT,
    symptom_check_ins INT,
    vitals_logged INT,

    -- Care Delivery
    appointments_scheduled INT,
    appointments_completed INT,
    telemedicine_visits INT,
    prescriptions_issued INT,
    lab_orders INT,

    -- Clinical Quality
    patients_on_care_pathways INT,
    pathway_completion_rate DECIMAL(3,2),
    chronic_disease_control_rate DECIMAL(3,2),
    preventive_screening_rate DECIMAL(3,2),

    -- Risk Management
    high_risk_patients INT,
    critical_alerts INT,
    human_review_tasks INT,
    average_alert_response_time_minutes INT,

    -- Financial
    claims_submitted INT,
    claims_approved INT,
    claims_denied INT,
    fraud_alerts INT,
    total_revenue DECIMAL(12,2),

    -- Technical
    system_uptime_percent DECIMAL(5,2),
    avg_api_latency_ms INT,
    ml_model_predictions INT
)
USING iceberg;
```

#### 4. ML Training Datasets

**Pre-aggregated datasets for ML model training:**

```sql
-- Readmission Risk Training Data
CREATE TABLE gold.ml_readmission_training (
    admission_id UUID,
    patient_id UUID,
    discharge_date DATE,

    -- Features
    age INT,
    gender STRING,
    primary_diagnosis STRING,
    comorbidity_count INT,
    length_of_stay_days INT,
    prior_admissions_1y INT,
    prior_admissions_6m INT,
    admission_type STRING,           -- ELECTIVE, EMERGENCY
    discharge_destination STRING,    -- HOME, SNF, REHAB
    lab_values STRUCT<
        bnp DECIMAL(10,2),
        creatinine DECIMAL(4,2),
        sodium INT,
        hemoglobin DECIMAL(3,1)
    >,
    medication_count INT,
    social_factors STRUCT<
        lives_alone BOOLEAN,
        transportation_access BOOLEAN
    >,

    -- Target
    readmitted_30d BOOLEAN,
    days_to_readmission INT
)
USING iceberg;
```

---

## 📊 BI Dashboards (Metabase / Apache Superset)

### Deployment:

**Metabase (Recommended for ease of use):**

```yaml
metabase:
  image: metabase/metabase:latest
  container_name: healthfinance-metabase
  ports:
    - "3001:3000"
  environment:
    MB_DB_TYPE: postgres
    MB_DB_DBNAME: metabase
    MB_DB_PORT: 5432
    MB_DB_USER: metabase
    MB_DB_PASS: metabase123
    MB_DB_HOST: postgres
  volumes:
    - metabase-data:/metabase-data
  networks:
    - healthfinance-network
```

**Data Source Connection:**
- Connect Metabase to Trino/Presto
- Trino connects to Iceberg tables
- Metabase queries Trino via SQL

### Dashboard Examples:

#### 1. Executive Dashboard

**KPIs:**
- Total Active Patients
- Daily Active Users
- Patient Satisfaction Score
- Revenue (MTD, YTD)
- Care Pathway Enrollment Rate
- Chronic Disease Control Rate

**Charts:**
- Patient Growth Trend (line chart)
- Revenue by Service Line (pie chart)
- Appointment Volume by Day of Week (bar chart)
- Top 10 Diagnoses (bar chart)

#### 2. Clinical Quality Dashboard

**Metrics:**
- Diabetes: % with HbA1c <7%
- Hypertension: % with BP <130/80
- Asthma: % with ACT >20
- Depression: % in remission (PHQ-9 <5)
- Colorectal Cancer Screening: % screened (age 50-75)
- Breast Cancer Screening: % screened (age 40-75)

**Drill-Down:**
- By provider
- By facility
- By payer

#### 3. Population Health Dashboard

**Metrics:**
- High-Risk Patient Count
- Readmission Rate (30-day, 90-day)
- Emergency Department Utilization Rate
- Preventable Hospitalization Rate

**Cohort Analysis:**
- Diabetic patients (stratified by control)
- Heart failure patients (stratified by risk)
- Multi-morbidity patients (2+, 3+, 4+ conditions)

#### 4. Provider Performance Dashboard

**Per Provider:**
- Patients Seen (MTD)
- Patient Satisfaction Score
- No-Show Rate
- Appointment Availability
- Quality Metrics (disease control rates)
- Revenue Generated

**Leaderboard:**
- Top 10 providers by patient satisfaction
- Top 10 by quality metrics

#### 5. Financial Dashboard

**Metrics:**
- Claims Submitted
- Claims Approved/Denied
- Denial Rate by Payer
- Days in A/R (accounts receivable)
- Collection Rate
- Fraud Detection Savings

**Charts:**
- Revenue Trend (monthly)
- Payer Mix (pie chart)
- Top 10 CPT Codes by Revenue

---

## 🍱 ML Feature Store (Feast)

### Purpose:
Centralized repository for ML features, ensuring consistency between training and inference.

### Architecture:

```
Feast Feature Store:
- Feature Definitions (Python code)
- Offline Store: S3/Parquet (historical data for training)
- Online Store: Redis (low-latency feature serving for real-time predictions)
- Feature Registry: PostgreSQL (metadata)
```

### Deployment:

```yaml
feast:
  image: feast/feast-python:latest
  container_name: healthfinance-feast
  ports:
    - "6566:6566"
  environment:
    FEAST_OFFLINE_STORE: file
    FEAST_OFFLINE_STORE_PATH: /data/offline
    FEAST_ONLINE_STORE: redis
    FEAST_REDIS_HOST: redis
    FEAST_REDIS_PORT: 6379
  volumes:
    - ./feast/feature_repo:/feature_repo
    - feast-data:/data
  networks:
    - healthfinance-network
```

### Feature Definitions:

**Example: Patient Demographics Features**

```python
# feature_repo/features.py

from feast import Entity, Feature, FeatureView, FileSource, ValueType
from datetime import timedelta

# Entity
patient = Entity(name="patient_id", value_type=ValueType.STRING, description="Patient ID")

# Feature Source (offline)
patient_demographics_source = FileSource(
    path="s3://healthfinance-datalake/gold/patient_demographics/",
    event_timestamp_column="updated_at",
    created_timestamp_column="created_at",
)

# Feature View
patient_demographics_fv = FeatureView(
    name="patient_demographics",
    entities=["patient_id"],
    ttl=timedelta(days=365),
    features=[
        Feature(name="age", dtype=ValueType.INT64),
        Feature(name="gender", dtype=ValueType.STRING),
        Feature(name="bmi", dtype=ValueType.DOUBLE),
        Feature(name="smoking_status", dtype=ValueType.STRING),
    ],
    online=True,
    batch_source=patient_demographics_source,
    tags={"team": "clinical-data"},
)
```

**Example: Clinical History Features**

```python
clinical_history_source = FileSource(
    path="s3://healthfinance-datalake/gold/clinical_features/",
    event_timestamp_column="feature_timestamp",
)

clinical_history_fv = FeatureView(
    name="clinical_history",
    entities=["patient_id"],
    ttl=timedelta(days=90),
    features=[
        Feature(name="hba1c_latest", dtype=ValueType.DOUBLE),
        Feature(name="bp_systolic_avg_30d", dtype=ValueType.INT64),
        Feature(name="bp_diastolic_avg_30d", dtype=ValueType.INT64),
        Feature(name="egfr_latest", dtype=ValueType.INT64),
        Feature(name="ldl_latest", dtype=ValueType.INT64),
        Feature(name="medication_count", dtype=ValueType.INT64),
        Feature(name="comorbidity_count", dtype=ValueType.INT64),
        Feature(name="prior_admissions_1y", dtype=ValueType.INT64),
    ],
    online=True,
    batch_source=clinical_history_source,
)
```

### Feature Retrieval:

**Training (Offline):**

```python
from feast import FeatureStore
import pandas as pd

store = FeatureStore(repo_path="feast/feature_repo")

# Entity dataframe (patients + timestamps for point-in-time joins)
entity_df = pd.DataFrame({
    "patient_id": ["patient-uuid-1", "patient-uuid-2", "patient-uuid-3"],
    "event_timestamp": ["2026-01-01", "2026-01-02", "2026-01-03"]
})

# Retrieve historical features
training_df = store.get_historical_features(
    entity_df=entity_df,
    features=[
        "patient_demographics:age",
        "patient_demographics:gender",
        "patient_demographics:bmi",
        "clinical_history:hba1c_latest",
        "clinical_history:bp_systolic_avg_30d",
        "clinical_history:medication_count",
    ]
).to_df()

# Use for training
X = training_df.drop(columns=["patient_id", "event_timestamp"])
y = training_df["target_variable"]  # e.g., readmitted_30d
```

**Inference (Online):**

```python
# Real-time feature retrieval (from Redis)
features = store.get_online_features(
    features=[
        "patient_demographics:age",
        "patient_demographics:bmi",
        "clinical_history:hba1c_latest",
        "clinical_history:medication_count",
    ],
    entity_rows=[{"patient_id": "patient-uuid-123"}]
).to_dict()

# Feed to ML model
prediction = model.predict([features])
```

**Materialization (Offline → Online):**

```bash
# Materialize features from offline store to online store (Redis)
feast materialize-incremental $(date -u +"%Y-%m-%dT%H:%M:%S")
```

**Schedule:** Daily cron job to materialize latest features

---

## 🔍 Data Quality Framework

### Principles:

1. **Completeness:** Required fields must not be NULL
2. **Validity:** Data within expected ranges
3. **Consistency:** Referential integrity maintained
4. **Timeliness:** Data freshness within SLA
5. **Accuracy:** Match source systems

### Implementation:

**Great Expectations (Python library):**

```python
import great_expectations as ge

# Load data
df = ge.read_csv("s3://healthfinance-datalake/silver/vitals_normalized/data.csv")

# Define expectations
df.expect_column_values_to_not_be_null("patient_id")
df.expect_column_values_to_be_between("bp_systolic", min_value=70, max_value=250)
df.expect_column_values_to_be_between("bp_diastolic", min_value=40, max_value=150)
df.expect_column_values_to_be_in_set("vital_type", ["BP", "HR", "TEMP", "WEIGHT", "SPO2"])

# Validate
results = df.validate()

# Report
if not results["success"]:
    # Alert data team
    print(f"Data quality check failed: {results}")
    # Send to Slack, PagerDuty, etc.
```

**Schedule:** Run after each silver layer batch job

### Data Quality Dashboard:

**Metrics:**
- Completeness Score (% non-null required fields)
- Validity Score (% within expected ranges)
- Consistency Score (% passing referential checks)
- Timeliness Score (% updated within SLA)
- Overall Data Quality Score (weighted average)

**Alerts:**
- Data quality score drops below 95% → Alert data engineering team
- Critical table has >10% NULL values → Immediate alert
- Data freshness >6 hours → Alert

---

## 🎯 Phase 6 Success Metrics

### Technical Metrics:
- ✅ All 27 services streaming via CDC
- ✅ CDC end-to-end latency <5 seconds p99
- ✅ Bronze layer ingestion: 1M+ events/day
- ✅ Silver/Gold batch jobs: <2 hour completion time
- ✅ BI dashboard query latency <3 seconds p95
- ✅ Feature store online serving latency <50ms p99

### Data Quality:
- ✅ Overall data quality score >95%
- ✅ Completeness >98% for critical fields
- ✅ Validity >95% for all fields
- ✅ Zero data loss incidents

### Business Impact:
- ✅ Patient 360 view available for 100% of patients
- ✅ 10+ operational dashboards deployed
- ✅ Data-driven insights used in weekly leadership meetings
- ✅ ML model training time reduced 50% (feature store)

---

## 📁 Deliverables

### Infrastructure:
- MinIO S3 bucket structure
- Apache Iceberg catalog
- 27 Debezium connectors
- Kafka Connect S3 sink
- Apache Spark cluster
- Trino/Presto query engine
- Metabase instance
- Feast feature store

### Data Pipelines:
- Bronze ingestion (real-time CDC)
- Silver transformation (hourly batch)
- Gold aggregation (daily batch)
- Feature materialization (daily)

### Data Assets:
- 27+ bronze tables (raw CDC)
- 50+ silver tables (cleaned)
- 10+ gold tables (aggregates)
- Patient 360 view
- 100+ Feast features

### Dashboards:
- Executive Dashboard
- Clinical Quality Dashboard
- Population Health Dashboard
- Provider Performance Dashboard
- Financial Dashboard
- Data Quality Dashboard

### Documentation:
- Data dictionary (all tables, columns)
- Data lineage diagrams
- Feature store guide
- Dashboard user guides
- Data quality standards
- Data governance policies

---

## 🚀 Next Phase Preview

**Phase 7 (Weeks 36-42): Observability & Reliability**

Will implement:
- Full observability stack (Prometheus, Grafana, ELK, Jaeger)
- Distributed tracing for all services
- SLO tracking and alerting
- Canary deployments (Argo Rollouts)
- Chaos testing framework
- Backup & disaster recovery
- Production readiness review

---

**Document Version:** 1.0
**Last Updated:** 2026-01-08
**Status:** ✅ Ready for Implementation
**Dependencies:** Phase 1, 2, 3, 4, 5 Complete
**Estimated Duration:** 6 weeks (Weeks 30-36)
