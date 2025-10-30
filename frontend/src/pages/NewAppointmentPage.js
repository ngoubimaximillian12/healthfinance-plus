import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { appointmentService } from '../services/api';
import { 
  CalendarIcon, 
  ClockIcon,
  UserIcon,
  DocumentTextIcon 
} from '@heroicons/react/24/outline';

const NewAppointmentPage = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    patientId: 'patient-' + Math.random().toString(36).substr(2, 9),
    doctorId: '',
    appointmentDate: '',
    appointmentTime: '',
    reason: '',
    notes: ''
  });

  const doctors = [
    { id: 'doctor-001', name: 'Dr. Sarah Johnson', specialty: 'Cardiologist' },
    { id: 'doctor-002', name: 'Dr. Michael Chen', specialty: 'General Practitioner' },
    { id: 'doctor-003', name: 'Dr. Emily Rodriguez', specialty: 'Pediatrician' },
    { id: 'doctor-004', name: 'Dr. James Williams', specialty: 'Orthopedist' },
    { id: 'doctor-005', name: 'Dr. Lisa Anderson', specialty: 'Dermatologist' },
  ];

  const timeSlots = [
    '09:00:00', '09:30:00', '10:00:00', '10:30:00', '11:00:00', '11:30:00',
    '14:00:00', '14:30:00', '15:00:00', '15:30:00', '16:00:00', '16:30:00'
  ];

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await appointmentService.create(formData);
      navigate('/appointments');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to book appointment. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const minDate = new Date().toISOString().split('T')[0];

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        <div className="max-w-3xl mx-auto">
          {/* Header */}
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-2">Book New Appointment</h1>
            <p className="text-gray-600">Schedule an appointment with your healthcare provider</p>
          </div>

          <div className="card">
            <form onSubmit={handleSubmit} className="space-y-6">
              {error && (
                <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
                  {error}
                </div>
              )}

              {/* Doctor Selection */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Select Doctor *
                </label>
                <div className="relative">
                  <UserIcon className="h-5 w-5 text-gray-400 absolute left-3 top-3.5" />
                  <select
                    name="doctorId"
                    required
                    value={formData.doctorId}
                    onChange={handleChange}
                    className="input-field pl-10"
                  >
                    <option value="">Choose a doctor...</option>
                    {doctors.map((doctor) => (
                      <option key={doctor.id} value={doctor.id}>
                        {doctor.name} - {doctor.specialty}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              {/* Date and Time */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Appointment Date *
                  </label>
                  <div className="relative">
                    <CalendarIcon className="h-5 w-5 text-gray-400 absolute left-3 top-3.5" />
                    <input
                      type="date"
                      name="appointmentDate"
                      required
                      min={minDate}
                      value={formData.appointmentDate}
                      onChange={handleChange}
                      className="input-field pl-10"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Appointment Time *
                  </label>
                  <div className="relative">
                    <ClockIcon className="h-5 w-5 text-gray-400 absolute left-3 top-3.5" />
                    <select
                      name="appointmentTime"
                      required
                      value={formData.appointmentTime}
                      onChange={handleChange}
                      className="input-field pl-10"
                    >
                      <option value="">Select time...</option>
                      {timeSlots.map((time) => (
                        <option key={time} value={time}>
                          {time.substring(0, 5)}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              </div>

              {/* Reason for Visit */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Reason for Visit *
                </label>
                <div className="relative">
                  <DocumentTextIcon className="h-5 w-5 text-gray-400 absolute left-3 top-3.5" />
                  <input
                    type="text"
                    name="reason"
                    required
                    value={formData.reason}
                    onChange={handleChange}
                    className="input-field pl-10"
                    placeholder="e.g., Annual checkup, Follow-up visit, Consultation"
                  />
                </div>
              </div>

              {/* Additional Notes */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Additional Notes (Optional)
                </label>
                <textarea
                  name="notes"
                  value={formData.notes}
                  onChange={handleChange}
                  rows="4"
                  className="input-field"
                  placeholder="Any additional information you'd like to share with your doctor..."
                />
              </div>

              {/* Important Information */}
              <div className="bg-primary-50 border border-primary-200 rounded-lg p-4">
                <h3 className="font-semibold text-primary-900 mb-2">📋 Before Your Appointment</h3>
                <ul className="text-sm text-primary-800 space-y-1">
                  <li>• Arrive 15 minutes early for check-in</li>
                  <li>• Bring your insurance card and ID</li>
                  <li>• List any current medications</li>
                  <li>• Prepare questions for your doctor</li>
                </ul>
              </div>

              {/* Action Buttons */}
              <div className="flex space-x-4">
                <button
                  type="submit"
                  disabled={loading}
                  className="flex-1 btn-primary"
                >
                  {loading ? 'Booking...' : 'Book Appointment'}
                </button>
                <button
                  type="button"
                  onClick={() => navigate('/appointments')}
                  className="flex-1 btn-secondary"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NewAppointmentPage;
