import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { appointmentService } from '../services/api';
import { 
  CalendarIcon, 
  PlusIcon,
  MagnifyingGlassIcon,
  FunnelIcon 
} from '@heroicons/react/24/outline';

const AppointmentsPage = () => {
  const [appointments, setAppointments] = useState([]);
  const [filteredAppointments, setFilteredAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');

  useEffect(() => {
    fetchAppointments();
  }, []);

  useEffect(() => {
    filterAppointments();
  }, [appointments, searchTerm, statusFilter]);

  const fetchAppointments = async () => {
    try {
      const response = await appointmentService.getAll();
      setAppointments(response.data);
    } catch (error) {
      console.error('Error fetching appointments:', error);
    } finally {
      setLoading(false);
    }
  };

  const filterAppointments = () => {
    let filtered = [...appointments];

    // Filter by status
    if (statusFilter !== 'ALL') {
      filtered = filtered.filter(apt => apt.status === statusFilter);
    }

    // Filter by search term
    if (searchTerm) {
      filtered = filtered.filter(apt => 
        apt.reason.toLowerCase().includes(searchTerm.toLowerCase()) ||
        apt.appointmentDate.includes(searchTerm) ||
        (apt.notes && apt.notes.toLowerCase().includes(searchTerm.toLowerCase()))
      );
    }

    setFilteredAppointments(filtered);
  };

  const handleCancelAppointment = async (id) => {
    if (window.confirm('Are you sure you want to cancel this appointment?')) {
      try {
        await appointmentService.cancel(id);
        fetchAppointments();
      } catch (error) {
        console.error('Error cancelling appointment:', error);
        alert('Failed to cancel appointment');
      }
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'SCHEDULED':
        return 'bg-blue-100 text-blue-800';
      case 'COMPLETED':
        return 'bg-green-100 text-green-800';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        {/* Header */}
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">My Appointments</h1>
            <p className="text-gray-600">Manage and track all your appointments</p>
          </div>
          <Link to="/appointments/new" className="btn-primary flex items-center space-x-2">
            <PlusIcon className="h-5 w-5" />
            <span>Book New Appointment</span>
          </Link>
        </div>

        {/* Filters */}
        <div className="card mb-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {/* Search */}
            <div className="relative">
              <MagnifyingGlassIcon className="h-5 w-5 text-gray-400 absolute left-3 top-3.5" />
              <input
                type="text"
                placeholder="Search appointments..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="input-field pl-10"
              />
            </div>

            {/* Status Filter */}
            <div className="relative">
              <FunnelIcon className="h-5 w-5 text-gray-400 absolute left-3 top-3.5" />
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="input-field pl-10"
              >
                <option value="ALL">All Status</option>
                <option value="SCHEDULED">Scheduled</option>
                <option value="COMPLETED">Completed</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>
          </div>
        </div>

        {/* Appointments List */}
        {loading ? (
          <div className="text-center py-12">
            <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-primary-600 mx-auto"></div>
          </div>
        ) : filteredAppointments.length === 0 ? (
          <div className="card text-center py-12">
            <CalendarIcon className="h-20 w-20 text-gray-300 mx-auto mb-4" />
            <h3 className="text-xl font-semibold text-gray-900 mb-2">No appointments found</h3>
            <p className="text-gray-600 mb-6">
              {searchTerm || statusFilter !== 'ALL' 
                ? 'Try adjusting your filters' 
                : 'Book your first appointment to get started'}
            </p>
            <Link to="/appointments/new" className="btn-primary inline-block">
              Book Appointment
            </Link>
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-4">
            {filteredAppointments.map((appointment) => (
              <div key={appointment.id} className="card hover:shadow-lg transition">
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center space-x-4 mb-3">
                      <div className="bg-primary-100 p-3 rounded-lg">
                        <CalendarIcon className="h-6 w-6 text-primary-600" />
                      </div>
                      <div>
                        <p className="text-lg font-semibold text-gray-900">{appointment.reason}</p>
                        <p className="text-gray-600">
                          {appointment.appointmentDate} at {appointment.appointmentTime}
                        </p>
                      </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                      <div>
                        <span className="text-gray-500">Doctor ID:</span>{' '}
                        <span className="font-medium">{appointment.doctorId}</span>
                      </div>
                      {appointment.notes && (
                        <div className="md:col-span-2">
                          <span className="text-gray-500">Notes:</span>{' '}
                          <span className="text-gray-700">{appointment.notes}</span>
                        </div>
                      )}
                    </div>
                  </div>

                  <div className="ml-4 flex flex-col items-end space-y-3">
                    <span className={`px-3 py-1 rounded-full text-xs font-semibold ${getStatusColor(appointment.status)}`}>
                      {appointment.status}
                    </span>
                    {appointment.status === 'SCHEDULED' && (
                      <div className="flex space-x-2">
                        <Link
                          to={`/appointments/${appointment.id}/edit`}
                          className="text-primary-600 hover:text-primary-700 font-medium text-sm"
                        >
                          Edit
                        </Link>
                        <button
                          onClick={() => handleCancelAppointment(appointment.id)}
                          className="text-red-600 hover:text-red-700 font-medium text-sm"
                        >
                          Cancel
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default AppointmentsPage;
