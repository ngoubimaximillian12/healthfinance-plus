import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { useAuth } from '../context/AuthContext';
import { appointmentService } from '../services/api';
import toast, { Toaster } from 'react-hot-toast';
import { 
  CalendarIcon, 
  ClockIcon,
  UserIcon,
  DocumentTextIcon,
  ChartBarIcon,
  ExclamationCircleIcon,
  SparklesIcon,
  BellIcon,
  ArrowTrendingUpIcon
} from '@heroicons/react/24/outline';

const DashboardPage = () => {
  const { user } = useAuth();
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedCard, setSelectedCard] = useState(null);
  const [stats, setStats] = useState({
    upcoming: 0,
    completed: 0,
    cancelled: 0,
    total: 0
  });

  useEffect(() => {
    fetchAppointments();
    toast.success(`Welcome back, ${user?.email?.split('@')[0]}!`, {
      icon: '👋',
      duration: 3000,
    });
  }, []);

  const fetchAppointments = async () => {
    try {
      const response = await appointmentService.getAll();
      const appts = response.data;
      setAppointments(appts.slice(0, 5));

      setStats({
        upcoming: appts.filter(a => a.status === 'SCHEDULED').length,
        completed: appts.filter(a => a.status === 'COMPLETED').length,
        cancelled: appts.filter(a => a.status === 'CANCELLED').length,
        total: appts.length
      });
    } catch (error) {
      console.error('Error fetching appointments:', error);
      toast.error('Failed to load appointments');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'SCHEDULED':
        return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'COMPLETED':
        return 'bg-green-100 text-green-800 border-green-200';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800 border-red-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const quickActions = [
    {
      icon: CalendarIcon,
      title: 'Book Appointment',
      description: 'Schedule a new appointment',
      link: '/appointments/new',
      gradient: 'from-blue-500 to-blue-600',
      iconBg: 'bg-blue-100',
      iconColor: 'text-blue-600'
    },
    {
      icon: DocumentTextIcon,
      title: 'Medical Records',
      description: 'View your records',
      link: '/records',
      gradient: 'from-green-500 to-green-600',
      iconBg: 'bg-green-100',
      iconColor: 'text-green-600'
    },
    {
      icon: UserIcon,
      title: 'Profile',
      description: 'Update your information',
      link: '/profile',
      gradient: 'from-purple-500 to-purple-600',
      iconBg: 'bg-purple-100',
      iconColor: 'text-purple-600'
    },
    {
      icon: ChartBarIcon,
      title: 'Reports',
      description: 'View health reports',
      link: '/reports',
      gradient: 'from-orange-500 to-orange-600',
      iconBg: 'bg-orange-100',
      iconColor: 'text-orange-600'
    }
  ];

  const statCards = [
    {
      title: 'Upcoming',
      value: stats.upcoming,
      icon: CalendarIcon,
      gradient: 'from-blue-500 to-blue-600',
      change: '+12%',
      changePositive: true
    },
    {
      title: 'Completed',
      value: stats.completed,
      icon: ClockIcon,
      gradient: 'from-green-500 to-green-600',
      change: '+8%',
      changePositive: true
    },
    {
      title: 'Cancelled',
      value: stats.cancelled,
      icon: ExclamationCircleIcon,
      gradient: 'from-red-500 to-red-600',
      change: '-3%',
      changePositive: false
    },
    {
      title: 'Total',
      value: stats.total,
      icon: ChartBarIcon,
      gradient: 'from-purple-500 to-purple-600',
      change: '+15%',
      changePositive: true
    }
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 py-8">
      <Toaster position="top-right" />
      <div className="container mx-auto px-4">
        {/* Welcome Section with Animation */}
        <motion.div 
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-8"
        >
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-4xl font-bold text-gray-900 mb-2 flex items-center">
                Welcome back, {user?.email?.split('@')[0]}! 
                <motion.span
                  animate={{ rotate: [0, 15, -15, 0] }}
                  transition={{ duration: 0.5, repeat: Infinity, repeatDelay: 3 }}
                  className="ml-3"
                >
                  👋
                </motion.span>
              </h1>
              <p className="text-gray-600 flex items-center">
                <SparklesIcon className="h-5 w-5 mr-2 text-primary-500" />
                Here's what's happening with your healthcare today
              </p>
            </div>
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              className="hidden md:flex items-center space-x-2 bg-primary-600 text-white px-6 py-3 rounded-lg hover:bg-primary-700 transition shadow-lg"
            >
              <BellIcon className="h-5 w-5" />
              <span>Notifications</span>
              <span className="bg-red-500 text-white text-xs rounded-full px-2 py-1">3</span>
            </motion.button>
          </div>
        </motion.div>

        {/* Animated Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          {statCards.map((stat, index) => (
            <motion.div
              key={index}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.1 }}
              whileHover={{ scale: 1.05, y: -5 }}
              onHoverStart={() => setSelectedCard(index)}
              onHoverEnd={() => setSelectedCard(null)}
              className="relative overflow-hidden cursor-pointer"
            >
              <div className={`card bg-gradient-to-br ${stat.gradient} text-white h-full`}>
                <div className="flex items-center justify-between mb-4">
                  <div>
                    <p className="text-white/80 text-sm font-medium">{stat.title}</p>
                    <motion.p 
                      initial={{ scale: 1 }}
                      animate={selectedCard === index ? { scale: 1.1 } : { scale: 1 }}
                      className="text-4xl font-bold"
                    >
                      {stat.value}
                    </motion.p>
                  </div>
                  <motion.div
                    animate={selectedCard === index ? { rotate: 360 } : { rotate: 0 }}
                    transition={{ duration: 0.5 }}
                  >
                    <stat.icon className="h-12 w-12 text-white/50" />
                  </motion.div>
                </div>
                <div className="flex items-center">
                  <ArrowTrendingUpIcon className={`h-4 w-4 mr-1 ${stat.changePositive ? '' : 'rotate-180'}`} />
                  <span className="text-sm">{stat.change} from last month</span>
                </div>
                
                {/* Animated background effect */}
                <motion.div
                  animate={{
                    scale: selectedCard === index ? [1, 1.5] : 1,
                    opacity: selectedCard === index ? [0.2, 0] : 0,
                  }}
                  transition={{ duration: 0.5 }}
                  className="absolute inset-0 bg-white rounded-xl"
                />
              </div>
            </motion.div>
          ))}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Recent Appointments with Loading Animation */}
          <div className="lg:col-span-2">
            <motion.div 
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              className="card bg-white"
            >
              <div className="flex items-center justify-between mb-6">
                <h2 className="text-2xl font-bold text-gray-900 flex items-center">
                  <CalendarIcon className="h-7 w-7 mr-2 text-primary-600" />
                  Recent Appointments
                </h2>
                <Link to="/appointments" className="text-primary-600 hover:text-primary-700 font-medium flex items-center group">
                  View All 
                  <motion.span
                    className="ml-1"
                    animate={{ x: [0, 5, 0] }}
                    transition={{ duration: 1, repeat: Infinity }}
                  >
                    →
                  </motion.span>
                </Link>
              </div>

              {loading ? (
                <div className="space-y-4">
                  {[1, 2, 3].map((i) => (
                    <motion.div
                      key={i}
                      initial={{ opacity: 0.5 }}
                      animate={{ opacity: [0.5, 1, 0.5] }}
                      transition={{ duration: 1.5, repeat: Infinity, delay: i * 0.2 }}
                      className="bg-gray-100 rounded-lg h-24 animate-pulse"
                    />
                  ))}
                </div>
              ) : appointments.length === 0 ? (
                <motion.div 
                  initial={{ opacity: 0, scale: 0.9 }}
                  animate={{ opacity: 1, scale: 1 }}
                  className="text-center py-12"
                >
                  <motion.div
                    animate={{ y: [0, -10, 0] }}
                    transition={{ duration: 2, repeat: Infinity }}
                  >
                    <CalendarIcon className="h-20 w-20 text-gray-300 mx-auto mb-4" />
                  </motion.div>
                  <p className="text-gray-500 mb-4">No appointments yet</p>
                  <Link to="/appointments/new" className="btn-primary inline-block">
                    Book Your First Appointment
                  </Link>
                </motion.div>
              ) : (
                <div className="space-y-4">
                  <AnimatePresence>
                    {appointments.map((appointment, index) => (
                      <motion.div
                        key={appointment.id}
                        initial={{ opacity: 0, x: -20 }}
                        animate={{ opacity: 1, x: 0 }}
                        exit={{ opacity: 0, x: 20 }}
                        transition={{ delay: index * 0.1 }}
                        whileHover={{ scale: 1.02, boxShadow: "0 10px 30px rgba(0,0,0,0.1)" }}
                        className="border-2 border-gray-100 rounded-xl p-5 hover:border-primary-200 transition-all duration-300 cursor-pointer bg-gradient-to-r from-white to-gray-50"
                      >
                        <div className="flex items-center justify-between">
                          <div className="flex-1">
                            <div className="flex items-center space-x-3 mb-3">
                              <div className="bg-primary-100 p-2 rounded-lg">
                                <CalendarIcon className="h-5 w-5 text-primary-600" />
                              </div>
                              <div>
                                <span className="font-semibold text-gray-900">{appointment.appointmentDate}</span>
                                <div className="flex items-center text-gray-600 text-sm">
                                  <ClockIcon className="h-4 w-4 mr-1" />
                                  {appointment.appointmentTime}
                                </div>
                              </div>
                            </div>
                            <p className="text-gray-700 mb-1">
                              <span className="font-medium">Reason:</span> {appointment.reason}
                            </p>
                            {appointment.notes && (
                              <p className="text-gray-600 text-sm">
                                <span className="font-medium">Notes:</span> {appointment.notes}
                              </p>
                            )}
                          </div>
                          <motion.div 
                            whileHover={{ scale: 1.1 }}
                            className="ml-4"
                          >
                            <span className={`px-4 py-2 rounded-full text-xs font-semibold border-2 ${getStatusColor(appointment.status)}`}>
                              {appointment.status}
                            </span>
                          </motion.div>
                        </div>
                      </motion.div>
                    ))}
                  </AnimatePresence>
                </div>
              )}
            </motion.div>
          </div>

          {/* Quick Actions & Health Tips */}
          <div className="space-y-6">
            {/* Quick Actions */}
            <motion.div 
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              className="card bg-white"
            >
              <h2 className="text-2xl font-bold text-gray-900 mb-6 flex items-center">
                <SparklesIcon className="h-7 w-7 mr-2 text-primary-600" />
                Quick Actions
              </h2>
              <div className="space-y-3">
                {quickActions.map((action, index) => (
                  <motion.div
                    key={index}
                    initial={{ opacity: 0, x: 20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: index * 0.1 }}
                    whileHover={{ scale: 1.03, x: 5 }}
                    whileTap={{ scale: 0.98 }}
                  >
                    <Link
                      to={action.link}
                      className="flex items-center space-x-4 p-4 border-2 border-gray-100 rounded-xl hover:border-primary-200 hover:shadow-md transition-all duration-300 group"
                    >
                      <motion.div 
                        whileHover={{ rotate: 360 }}
                        transition={{ duration: 0.5 }}
                        className={`${action.iconBg} p-3 rounded-xl group-hover:shadow-lg transition-shadow`}
                      >
                        <action.icon className={`h-6 w-6 ${action.iconColor}`} />
                      </motion.div>
                      <div className="flex-1">
                        <p className="font-semibold text-gray-900">{action.title}</p>
                        <p className="text-sm text-gray-600">{action.description}</p>
                      </div>
                      <motion.div
                        animate={{ x: [0, 5, 0] }}
                        transition={{ duration: 1, repeat: Infinity }}
                        className="opacity-0 group-hover:opacity-100 transition-opacity"
                      >
                        →
                      </motion.div>
                    </Link>
                  </motion.div>
                ))}
              </div>
            </motion.div>

            {/* Animated Health Tip */}
            <motion.div 
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              whileHover={{ scale: 1.03 }}
              className="card bg-gradient-to-br from-primary-50 to-primary-100 border-2 border-primary-200 relative overflow-hidden"
            >
              <motion.div
                animate={{
                  scale: [1, 1.2, 1],
                  opacity: [0.3, 0.5, 0.3],
                }}
                transition={{
                  duration: 3,
                  repeat: Infinity,
                }}
                className="absolute top-0 right-0 w-32 h-32 bg-primary-200 rounded-full -translate-y-1/2 translate-x-1/2"
              />
              <div className="relative z-10">
                <h3 className="text-lg font-bold text-primary-900 mb-3 flex items-center">
                  <motion.span
                    animate={{ rotate: [0, 10, -10, 0] }}
                    transition={{ duration: 2, repeat: Infinity }}
                    className="text-2xl mr-2"
                  >
                    💡
                  </motion.span>
                  Daily Health Tip
                </h3>
                <p className="text-primary-800">
                  Remember to stay hydrated! Drinking 8 glasses of water daily helps maintain good health and boosts your energy levels.
                </p>
              </div>
            </motion.div>

            {/* Upcoming Reminder */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              whileHover={{ scale: 1.03 }}
              className="card bg-gradient-to-br from-orange-50 to-orange-100 border-2 border-orange-200"
            >
              <div className="flex items-start space-x-3">
                <motion.div
                  animate={{ scale: [1, 1.2, 1] }}
                  transition={{ duration: 1, repeat: Infinity }}
                >
                  <BellIcon className="h-6 w-6 text-orange-600" />
                </motion.div>
                <div>
                  <h3 className="font-bold text-orange-900 mb-1">Reminder</h3>
                  <p className="text-sm text-orange-800">
                    You have an appointment coming up in 2 days. Don't forget to prepare your medical history!
                  </p>
                </div>
              </div>
            </motion.div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;
