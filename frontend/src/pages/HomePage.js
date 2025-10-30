import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import { motion, useScroll, useTransform, useInView } from 'framer-motion';
import toast, { Toaster } from 'react-hot-toast';
import { 
  CalendarIcon, 
  DocumentTextIcon, 
  CreditCardIcon,
  ShieldCheckIcon,
  ClockIcon,
  UserGroupIcon,
  CheckCircleIcon,
  StarIcon,
  ArrowRightIcon,
  PlayIcon,
  ChatBubbleLeftRightIcon,
  XMarkIcon,
  ChevronDownIcon,
  CheckIcon,
  SparklesIcon,
  BellIcon,
  PaperAirplaneIcon
} from '@heroicons/react/24/outline';

const HomePage = () => {
  const [activeFeature, setActiveFeature] = useState(0);
  const [showVideo, setShowVideo] = useState(false);
  const [showChat, setShowChat] = useState(false);
  const [expandedFaq, setExpandedFaq] = useState(null);
  const [email, setEmail] = useState('');
  const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });
  const { scrollYProgress } = useScroll();
  const scaleProgress = useTransform(scrollYProgress, [0, 1], [0, 1]);

  const [stats, setStats] = useState({
    patients: 0,
    providers: 0,
    appointments: 0,
    satisfaction: 0
  });

  // Mouse tracking
  useEffect(() => {
    const handleMouseMove = (e) => {
      setMousePosition({ x: e.clientX, y: e.clientY });
    };
    window.addEventListener('mousemove', handleMouseMove);
    return () => window.removeEventListener('mousemove', handleMouseMove);
  }, []);

  // Animate stats
  useEffect(() => {
    const duration = 2000;
    const steps = 60;
    const interval = duration / steps;

    const targets = {
      patients: 10000,
      providers: 500,
      appointments: 50000,
      satisfaction: 98
    };

    let step = 0;
    const timer = setInterval(() => {
      step++;
      setStats({
        patients: Math.floor((targets.patients / steps) * step),
        providers: Math.floor((targets.providers / steps) * step),
        appointments: Math.floor((targets.appointments / steps) * step),
        satisfaction: Math.floor((targets.satisfaction / steps) * step)
      });

      if (step >= steps) clearInterval(timer);
    }, interval);

    return () => clearInterval(timer);
  }, []);

  // Auto-cycle features
  useEffect(() => {
    const interval = setInterval(() => {
      setActiveFeature((prev) => (prev + 1) % 6);
    }, 3000);
    return () => clearInterval(interval);
  }, []);

  const features = [
    {
      icon: CalendarIcon,
      title: 'Easy Appointment Booking',
      description: 'Schedule appointments with your healthcare providers quickly and efficiently.',
      color: 'from-blue-500 to-blue-600',
      gradient: 'bg-gradient-to-br from-blue-50 to-blue-100',
      image: '📅'
    },
    {
      icon: DocumentTextIcon,
      title: 'Medical Records Management',
      description: 'Access and manage all your medical records securely in one place.',
      color: 'from-green-500 to-green-600',
      gradient: 'bg-gradient-to-br from-green-50 to-green-100',
      image: '📋'
    },
    {
      icon: CreditCardIcon,
      title: 'Billing & Payments',
      description: 'Track medical expenses and manage payments seamlessly.',
      color: 'from-purple-500 to-purple-600',
      gradient: 'bg-gradient-to-br from-purple-50 to-purple-100',
      image: '��'
    },
    {
      icon: ShieldCheckIcon,
      title: 'Insurance Claims',
      description: 'Submit and track insurance claims with ease.',
      color: 'from-orange-500 to-orange-600',
      gradient: 'bg-gradient-to-br from-orange-50 to-orange-100',
      image: '🛡️'
    },
    {
      icon: ClockIcon,
      title: '24/7 Support',
      description: 'Our support team is always available to help you.',
      color: 'from-red-500 to-red-600',
      gradient: 'bg-gradient-to-br from-red-50 to-red-100',
      image: '⏰'
    },
    {
      icon: UserGroupIcon,
      title: 'Family Management',
      description: 'Manage healthcare for your entire family from one account.',
      color: 'from-pink-500 to-pink-600',
      gradient: 'bg-gradient-to-br from-pink-50 to-pink-100',
      image: '👨‍👩‍👧‍👦'
    },
  ];

  const testimonials = [
    {
      name: 'Sarah Johnson',
      role: 'Patient',
      image: '👩‍⚕️',
      comment: 'HealthFinancePlus has made managing my healthcare so much easier. I can book appointments and track everything in one place!',
      rating: 5,
      location: 'New York, NY'
    },
    {
      name: 'Dr. Michael Chen',
      role: 'Healthcare Provider',
      image: '👨‍⚕️',
      comment: 'The platform streamlines my appointment scheduling and patient management. Highly recommended!',
      rating: 5,
      location: 'Los Angeles, CA'
    },
    {
      name: 'Emily Rodriguez',
      role: 'Patient',
      image: '👩',
      comment: 'The insurance claim tracking feature saved me so much time and hassle. Outstanding service!',
      rating: 5,
      location: 'Chicago, IL'
    },
  ];

  const faqs = [
    {
      question: 'How do I book an appointment?',
      answer: 'Simply sign up for an account, browse available doctors, select a time slot that works for you, and confirm your booking. You\'ll receive instant confirmation and reminders.'
    },
    {
      question: 'Is my health data secure?',
      answer: 'Absolutely! We use bank-level encryption and comply with HIPAA regulations. Your data is stored securely and never shared without your explicit consent.'
    },
    {
      question: 'Can I manage my family\'s healthcare?',
      answer: 'Yes! You can add family members to your account and manage all their appointments, records, and insurance in one convenient place.'
    },
    {
      question: 'What payment methods do you accept?',
      answer: 'We accept all major credit cards, debit cards, HSA/FSA cards, and offer payment plans for larger expenses.'
    },
    {
      question: 'Do you offer mobile apps?',
      answer: 'Yes! Our mobile apps for iOS and Android provide full functionality so you can manage your healthcare on the go.'
    }
  ];

  const pricingPlans = [
    {
      name: 'Basic',
      price: '$0',
      period: '/month',
      description: 'Perfect for individuals',
      features: [
        'Up to 5 appointments/year',
        'Basic record storage',
        'Email support',
        'Mobile access',
        'Health tracking'
      ],
      popular: false,
      color: 'from-gray-500 to-gray-600'
    },
    {
      name: 'Pro',
      price: '$29',
      period: '/month',
      description: 'Best for families',
      features: [
        'Unlimited appointments',
        'Priority scheduling',
        '24/7 phone support',
        'Family management (up to 5)',
        'Insurance integration',
        'Advanced analytics',
        'Telemedicine access'
      ],
      popular: true,
      color: 'from-primary-500 to-blue-600'
    },
    {
      name: 'Enterprise',
      price: '$99',
      period: '/month',
      description: 'For healthcare organizations',
      features: [
        'All Pro features',
        'Dedicated account manager',
        'Custom integrations',
        'Advanced analytics',
        'White-label options',
        'API access',
        'Unlimited users',
        'Priority support'
      ],
      popular: false,
      color: 'from-purple-500 to-purple-600'
    }
  ];

  const handleNewsletterSubmit = (e) => {
    e.preventDefault();
    if (email) {
      toast.success('🎉 Thanks for subscribing!');
      setEmail('');
    }
  };

  const ParallaxSection = ({ children, offset = 50 }) => {
    const ref = useRef(null);
    const isInView = useInView(ref, { once: false, margin: "-100px" });
    
    return (
      <motion.div
        ref={ref}
        initial={{ opacity: 0, y: offset }}
        animate={isInView ? { opacity: 1, y: 0 } : { opacity: 0, y: offset }}
        transition={{ duration: 0.6 }}
      >
        {children}
      </motion.div>
    );
  };

  return (
    <div className="overflow-hidden relative">
      <Toaster position="top-right" />
      
      {/* Scroll Progress Bar */}
      <motion.div
        style={{ scaleX: scaleProgress }}
        className="fixed top-0 left-0 right-0 h-1 bg-gradient-to-r from-primary-500 to-blue-500 origin-left z-50"
      />

      {/* Hero Section */}
      <section className="relative bg-gradient-to-r from-primary-600 via-primary-700 to-primary-800 text-white py-20 md:py-32 overflow-hidden">
        {/* Animated Background Particles */}
        <div className="absolute inset-0 overflow-hidden">
          {[...Array(20)].map((_, i) => (
            <motion.div
              key={i}
              className="absolute w-2 h-2 bg-white rounded-full"
              initial={{
                x: Math.random() * (typeof window !== 'undefined' ? window.innerWidth : 1000),
                y: Math.random() * 800,
                opacity: Math.random() * 0.5
              }}
              animate={{
                y: [null, Math.random() * -500],
                opacity: [null, 0]
              }}
              transition={{
                duration: Math.random() * 3 + 2,
                repeat: Infinity,
                delay: Math.random() * 2
              }}
            />
          ))}
        </div>

        <div className="container mx-auto px-4 relative z-10">
          <motion.div 
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            className="max-w-4xl mx-auto text-center"
          >
            <motion.div
              initial={{ scale: 0 }}
              animate={{ scale: 1 }}
              transition={{ type: "spring", stiffness: 100, delay: 0.2 }}
              className="inline-block mb-6"
            >
              <span className="bg-white/20 backdrop-blur-lg text-white px-6 py-2 rounded-full text-sm font-semibold">
                ⭐ Trusted by 10,000+ Patients
              </span>
            </motion.div>

            <motion.h1 
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.5 }}
              className="text-5xl md:text-7xl font-bold mb-6 leading-tight"
            >
              Your Health, Our{' '}
              <motion.span
                className="text-primary-200"
                animate={{ 
                  textShadow: [
                    "0 0 20px rgba(255,255,255,0.5)",
                    "0 0 40px rgba(255,255,255,0.8)",
                    "0 0 20px rgba(255,255,255,0.5)"
                  ]
                }}
                transition={{ duration: 2, repeat: Infinity }}
              >
                Priority
              </motion.span>
            </motion.h1>

            <motion.p 
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ delay: 0.3, duration: 0.8 }}
              className="text-xl md:text-2xl mb-8 text-primary-100"
            >
              Comprehensive healthcare management platform that simplifies everything
            </motion.p>

            <motion.div 
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.6, duration: 0.8 }}
              className="flex flex-col sm:flex-row justify-center gap-4"
            >
              <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
                <Link 
                  to="/register" 
                  className="group bg-white text-primary-600 px-8 py-4 rounded-lg font-semibold text-lg hover:bg-gray-100 transition shadow-lg hover:shadow-2xl flex items-center justify-center"
                >
                  Get Started Free
                  <ArrowRightIcon className="ml-2 h-5 w-5 group-hover:translate-x-2 transition-transform" />
                </Link>
              </motion.div>

              <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
                <button 
                  onClick={() => setShowVideo(true)}
                  className="group border-2 border-white text-white px-8 py-4 rounded-lg font-semibold text-lg hover:bg-white hover:text-primary-600 transition duration-300 flex items-center justify-center backdrop-blur-lg bg-white/10"
                >
                  <PlayIcon className="mr-2 h-5 w-5" />
                  Watch Demo
                </button>
              </motion.div>
            </motion.div>
          </motion.div>
        </div>

        {/* Scroll Indicator */}
        <motion.div
          animate={{ y: [0, 10, 0] }}
          transition={{ duration: 1.5, repeat: Infinity }}
          className="absolute bottom-8 left-1/2 transform -translate-x-1/2"
        >
          <ChevronDownIcon className="h-8 w-8 text-white/50" />
        </motion.div>
      </section>

      {/* Features Section */}
      <ParallaxSection>
        <section className="py-20 bg-gradient-to-b from-gray-50 to-white">
          <div className="container mx-auto px-4">
            <motion.div 
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              className="text-center mb-16"
            >
              <h2 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4">
                Why Choose <span className="text-primary-600">HealthFinancePlus?</span>
              </h2>
              <p className="text-xl text-gray-600 max-w-2xl mx-auto">
                We provide everything you need to manage your healthcare efficiently
              </p>
            </motion.div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
              {features.map((feature, index) => (
                <motion.div
                  key={index}
                  initial={{ opacity: 0, y: 50 }}
                  whileInView={{ opacity: 1, y: 0 }}
                  viewport={{ once: true }}
                  transition={{ delay: index * 0.1 }}
                  whileHover={{ scale: 1.05, y: -10 }}
                  onHoverStart={() => setActiveFeature(index)}
                  className="relative group cursor-pointer"
                >
                  <div className={`card h-full ${feature.gradient} border-2 border-transparent group-hover:border-primary-300 transition-all duration-300`}>
                    <motion.div
                      animate={activeFeature === index ? { rotate: 360 } : {}}
                      transition={{ duration: 0.5 }}
                      className={`inline-block bg-gradient-to-br ${feature.color} p-4 rounded-xl mb-4 shadow-lg`}
                    >
                      <feature.icon className="h-8 w-8 text-white" />
                    </motion.div>

                    <span className="text-4xl mb-3 block">{feature.image}</span>
                    <h3 className="text-xl font-semibold mb-3 text-gray-900">{feature.title}</h3>
                    <p className="text-gray-600">{feature.description}</p>
                  </div>
                </motion.div>
              ))}
            </div>
          </div>
        </section>
      </ParallaxSection>

      {/* Animated Stats */}
      <section className="py-20 bg-gradient-to-r from-primary-600 via-primary-700 to-primary-800 text-white">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8 text-center">
            {[
              { value: stats.patients, label: 'Active Patients', suffix: '+' },
              { value: stats.providers, label: 'Healthcare Providers', suffix: '+' },
              { value: stats.appointments, label: 'Appointments Booked', suffix: '+' },
              { value: stats.satisfaction, label: 'Satisfaction Rate', suffix: '%' }
            ].map((stat, index) => (
              <motion.div
                key={index}
                initial={{ scale: 0 }}
                whileInView={{ scale: 1 }}
                viewport={{ once: true }}
                transition={{ type: "spring", stiffness: 100, delay: index * 0.1 }}
                whileHover={{ scale: 1.1 }}
              >
                <div className="text-6xl font-bold mb-2">
                  {stat.value.toLocaleString()}{stat.suffix}
                </div>
                <div className="text-primary-200">{stat.label}</div>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* Pricing Section */}
      <ParallaxSection>
        <section className="py-20 bg-gradient-to-b from-white to-gray-50">
          <div className="container mx-auto px-4">
            <motion.div 
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              className="text-center mb-16"
            >
              <motion.span
                initial={{ opacity: 0, scale: 0.5 }}
                whileInView={{ opacity: 1, scale: 1 }}
                viewport={{ once: true }}
                className="inline-block text-primary-600 font-semibold mb-2"
              >
                PRICING PLANS
              </motion.span>
              <h2 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4">
                Choose Your <span className="text-primary-600">Perfect Plan</span>
              </h2>
              <p className="text-xl text-gray-600">
                Flexible pricing that grows with your needs
              </p>
            </motion.div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl mx-auto">
              {pricingPlans.map((plan, index) => (
                <motion.div
                  key={index}
                  initial={{ opacity: 0, y: 50 }}
                  whileInView={{ opacity: 1, y: 0 }}
                  viewport={{ once: true }}
                  transition={{ delay: index * 0.1 }}
                  whileHover={{ y: -10, scale: 1.02 }}
                  className={`relative ${plan.popular ? 'md:scale-105 z-10' : ''}`}
                >
                  {plan.popular && (
                    <motion.div
                      initial={{ opacity: 0, y: -10 }}
                      animate={{ opacity: 1, y: 0 }}
                      className="absolute -top-5 left-1/2 transform -translate-x-1/2"
                    >
                      <span className="bg-gradient-to-r from-primary-500 to-blue-500 text-white px-6 py-2 rounded-full text-sm font-semibold shadow-lg flex items-center">
                        <SparklesIcon className="h-4 w-4 mr-1" />
                        Most Popular
                      </span>
                    </motion.div>
                  )}

                  <div className={`card h-full ${plan.popular ? 'border-2 border-primary-500 shadow-2xl' : 'border border-gray-200'} bg-white relative overflow-hidden`}>
                    {/* Background gradient effect */}
                    <div className={`absolute top-0 right-0 w-32 h-32 bg-gradient-to-br ${plan.color} opacity-10 rounded-full -translate-y-1/2 translate-x-1/2`} />

                    <div className="relative z-10">
                      <h3 className="text-2xl font-bold text-gray-900 mb-2">{plan.name}</h3>
                      <p className="text-gray-600 text-sm mb-6">{plan.description}</p>

                      <div className="mb-6">
                        <span className="text-5xl font-bold text-gray-900">{plan.price}</span>
                        <span className="text-gray-600">{plan.period}</span>
                      </div>

                      <ul className="space-y-4 mb-8">
                        {plan.features.map((feature, i) => (
                          <motion.li
                            key={i}
                            initial={{ opacity: 0, x: -20 }}
                            whileInView={{ opacity: 1, x: 0 }}
                            viewport={{ once: true }}
                            transition={{ delay: i * 0.05 }}
                            className="flex items-start"
                          >
                            <CheckIcon className="h-5 w-5 text-green-500 mr-3 flex-shrink-0 mt-0.5" />
                            <span className="text-gray-700">{feature}</span>
                          </motion.li>
                        ))}
                      </ul>

                      <motion.button
                        whileHover={{ scale: 1.05 }}
                        whileTap={{ scale: 0.95 }}
                        className={`w-full py-3 px-6 rounded-lg font-semibold transition-all ${
                          plan.popular
                            ? 'bg-gradient-to-r from-primary-600 to-blue-600 text-white shadow-lg hover:shadow-xl'
                            : 'bg-gray-100 text-gray-900 hover:bg-gray-200'
                        }`}
                      >
                        Get Started
                      </motion.button>
                    </div>
                  </div>
                </motion.div>
              ))}
            </div>

            <motion.p
              initial={{ opacity: 0 }}
              whileInView={{ opacity: 1 }}
              viewport={{ once: true }}
              className="text-center text-gray-600 mt-12"
            >
              All plans include a 30-day money-back guarantee. No questions asked.
            </motion.p>
          </div>
        </section>
      </ParallaxSection>

      {/* Testimonials */}
      <ParallaxSection>
        <section className="py-20 bg-gray-50">
          <div className="container mx-auto px-4">
            <motion.div 
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              className="text-center mb-16"
            >
              <h2 className="text-4xl font-bold text-gray-900 mb-4">
                What Our <span className="text-primary-600">Users Say</span>
              </h2>
              <p className="text-xl text-gray-600">
                Don't just take our word for it
              </p>
            </motion.div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
              {testimonials.map((testimonial, index) => (
                <motion.div
                  key={index}
                  initial={{ opacity: 0, y: 50 }}
                  whileInView={{ opacity: 1, y: 0 }}
                  viewport={{ once: true }}
                  transition={{ delay: index * 0.2 }}
                  whileHover={{ y: -10, scale: 1.02 }}
                  className="card bg-white hover:shadow-2xl transition-all duration-300"
                >
                  <div className="flex items-center mb-4">
                    <div className="text-5xl mr-4">{testimonial.image}</div>
                    <div>
                      <div className="font-semibold text-lg">{testimonial.name}</div>
                      <div className="text-sm text-gray-500">{testimonial.role}</div>
                      <div className="text-xs text-gray-400">{testimonial.location}</div>
                    </div>
                  </div>
                  <div className="flex mb-4">
                    {[...Array(testimonial.rating)].map((_, i) => (
                      <StarIcon key={i} className="h-5 w-5 text-yellow-400 fill-current" />
                    ))}
                  </div>
                  <p className="text-gray-600 italic">"{testimonial.comment}"</p>
                </motion.div>
              ))}
            </div>
          </div>
        </section>
      </ParallaxSection>

      {/* FAQ Section */}
      <ParallaxSection>
        <section className="py-20 bg-white">
          <div className="container mx-auto px-4 max-w-4xl">
            <motion.div 
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              className="text-center mb-16"
            >
              <motion.span
                initial={{ opacity: 0, scale: 0.5 }}
                whileInView={{ opacity: 1, scale: 1 }}
                viewport={{ once: true }}
                className="inline-block text-primary-600 font-semibold mb-2"
              >
                FAQ
              </motion.span>
              <h2 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4">
                Frequently Asked <span className="text-primary-600">Questions</span>
              </h2>
              <p className="text-xl text-gray-600">
                Everything you need to know about HealthFinancePlus
              </p>
            </motion.div>

            <div className="space-y-4">
              {faqs.map((faq, index) => (
                <motion.div
                  key={index}
                  initial={{ opacity: 0, y: 20 }}
                  whileInView={{ opacity: 1, y: 0 }}
                  viewport={{ once: true }}
                  transition={{ delay: index * 0.1 }}
                  className="border-2 border-gray-200 rounded-xl overflow-hidden hover:border-primary-300 transition-colors"
                >
                  <motion.button
                    onClick={() => setExpandedFaq(expandedFaq === index ? null : index)}
                    className="w-full px-6 py-5 flex items-center justify-between text-left hover:bg-gray-50 transition-colors"
                    whileHover={{ backgroundColor: 'rgba(0,0,0,0.02)' }}
                  >
                    <span className="font-semibold text-gray-900 pr-8">{faq.question}</span>
                    <motion.div
                      animate={{ rotate: expandedFaq === index ? 180 : 0 }}
                      transition={{ duration: 0.3 }}
                    >
                      <ChevronDownIcon className="h-5 w-5 text-primary-600 flex-shrink-0" />
                    </motion.div>
                  </motion.button>

                  <motion.div
                    initial={false}
                    animate={{
                      height: expandedFaq === index ? 'auto' : 0,
                      opacity: expandedFaq === index ? 1 : 0
                    }}
                    transition={{ duration: 0.3 }}
                    className="overflow-hidden"
                  >
                    <div className="px-6 pb-5 text-gray-600 bg-gray-50">
                      {faq.answer}
                    </div>
                  </motion.div>
                </motion.div>
              ))}
            </div>

            <motion.div
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              className="text-center mt-12"
            >
              <p className="text-gray-600 mb-4">Still have questions?</p>
              <Link to="/contact" className="text-primary-600 hover:text-primary-700 font-semibold">
                Contact our support team →
              </Link>
            </motion.div>
          </div>
        </section>
      </ParallaxSection>

      {/* Newsletter Section */}
      <ParallaxSection>
        <section className="py-20 bg-gradient-to-br from-primary-600 via-primary-700 to-blue-600 relative overflow-hidden">
          {/* Animated background elements */}
          <motion.div
            animate={{ scale: [1, 1.2, 1], rotate: [0, 90, 0] }}
            transition={{ duration: 10, repeat: Infinity }}
            className="absolute top-0 right-0 w-96 h-96 bg-white opacity-5 rounded-full"
          />
          <motion.div
            animate={{ scale: [1.2, 1, 1.2], rotate: [90, 0, 90] }}
            transition={{ duration: 8, repeat: Infinity }}
            className="absolute bottom-0 left-0 w-96 h-96 bg-white opacity-5 rounded-full"
          />

          <div className="container mx-auto px-4 relative z-10">
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              className="max-w-3xl mx-auto text-center"
            >
              <motion.div
                animate={{ rotate: [0, 10, -10, 0] }}
                transition={{ duration: 2, repeat: Infinity }}
                className="inline-block mb-6"
              >
                <BellIcon className="h-16 w-16 text-white" />
              </motion.div>

              <h2 className="text-4xl md:text-5xl font-bold text-white mb-4">
                Stay Updated with Health Tips
              </h2>
              <p className="text-xl text-primary-100 mb-8">
                Subscribe to our newsletter for the latest healthcare insights, tips, and exclusive offers
              </p>

              <form onSubmit={handleNewsletterSubmit} className="max-w-md mx-auto">
                <div className="flex flex-col sm:flex-row gap-4">
                  <div className="flex-1 relative">
                    <input
                      type="email"
                      required
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      placeholder="Enter your email"
                      className="w-full px-6 py-4 rounded-lg focus:ring-4 focus:ring-white/50 focus:outline-none text-gray-900"
                    />
                  </div>
                  <motion.button
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                    type="submit"
                    className="bg-white text-primary-600 px-8 py-4 rounded-lg font-semibold hover:bg-gray-100 transition shadow-lg flex items-center justify-center"
                  >
                    Subscribe
                    <PaperAirplaneIcon className="ml-2 h-5 w-5" />
                  </motion.button>
                </div>
              </form>

              <p className="text-primary-100 text-sm mt-6">
                🔒 We respect your privacy. Unsubscribe at any time.
              </p>

              {/* Social proof */}
              <motion.div
                initial={{ opacity: 0 }}
                whileInView={{ opacity: 1 }}
                viewport={{ once: true }}
                className="mt-12 flex justify-center items-center space-x-2 text-white"
              >
                <div className="flex -space-x-2">
                  {['👨', '👩', '👴', '👧'].map((emoji, i) => (
                    <motion.div
                      key={i}
                      initial={{ scale: 0 }}
                      whileInView={{ scale: 1 }}
                      viewport={{ once: true }}
                      transition={{ delay: i * 0.1 }}
                      className="w-10 h-10 rounded-full bg-white/20 backdrop-blur-lg flex items-center justify-center text-xl border-2 border-white"
                    >
                      {emoji}
                    </motion.div>
                  ))}
                </div>
                <span className="ml-4 text-sm">Join 5,000+ subscribers</span>
              </motion.div>
            </motion.div>
          </div>
        </section>
      </ParallaxSection>

      {/* Final CTA */}
      <section className="py-20 bg-white">
        <div className="container mx-auto px-4">
          <motion.div 
            initial={{ opacity: 0, scale: 0.9 }}
            whileInView={{ opacity: 1, scale: 1 }}
            viewport={{ once: true }}
            whileHover={{ scale: 1.02 }}
            className="bg-gradient-to-r from-primary-500 via-primary-600 to-primary-700 rounded-3xl p-12 md:p-16 text-center text-white relative overflow-hidden shadow-2xl"
          >
            <motion.div
              animate={{ scale: [1, 1.5, 1], opacity: [0.3, 0.6, 0.3] }}
              transition={{ duration: 4, repeat: Infinity }}
              className="absolute top-0 left-0 w-64 h-64 bg-white rounded-full -translate-x-1/2 -translate-y-1/2"
            />

            <div className="relative z-10">
              <h2 className="text-4xl md:text-5xl font-bold mb-4">
                Ready to Get Started?
              </h2>
              <p className="text-xl mb-8 text-primary-100">
                Join thousands of satisfied users managing their healthcare with ease
              </p>
              <Link to="/register" className="inline-block bg-white text-primary-600 px-8 py-4 rounded-lg font-semibold text-lg hover:bg-gray-100 transition shadow-lg hover:shadow-2xl transform hover:-translate-y-1 duration-300">
                Create Your Account Now
                <ArrowRightIcon className="inline-block ml-2 h-5 w-5" />
              </Link>
            </div>
          </motion.div>
        </div>
      </section>

      {/* Video Modal */}
      {showVideo && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          className="fixed inset-0 bg-black/80 backdrop-blur-sm z-50 flex items-center justify-center p-4"
          onClick={() => setShowVideo(false)}
        >
          <motion.div
            initial={{ scale: 0.9, y: 20 }}
            animate={{ scale: 1, y: 0 }}
            className="relative max-w-4xl w-full bg-white rounded-2xl overflow-hidden"
            onClick={(e) => e.stopPropagation()}
          >
            <button
              onClick={() => setShowVideo(false)}
              className="absolute top-4 right-4 bg-white/20 backdrop-blur-lg text-white p-2 rounded-full hover:bg-white/30 transition z-10"
            >
              <XMarkIcon className="h-6 w-6" />
            </button>
            <div className="aspect-video bg-gray-900 flex items-center justify-center">
              <PlayIcon className="h-20 w-20 text-white opacity-50" />
              <p className="text-white ml-4">Demo Video Player</p>
            </div>
          </motion.div>
        </motion.div>
      )}

      {/* Floating Chat Button */}
      <motion.button
        whileHover={{ scale: 1.1 }}
        whileTap={{ scale: 0.9 }}
        onClick={() => setShowChat(!showChat)}
        className="fixed bottom-6 right-6 bg-gradient-to-r from-primary-600 to-blue-600 text-white p-4 rounded-full shadow-2xl z-40"
        animate={!showChat ? { y: [0, -10, 0] } : {}}
        transition={{ duration: 2, repeat: Infinity }}
      >
        {showChat ? <XMarkIcon className="h-6 w-6" /> : <ChatBubbleLeftRightIcon className="h-6 w-6" />}
        <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
          1
        </span>
      </motion.button>

      {/* Chat Widget */}
      {showChat && (
        <motion.div
          initial={{ opacity: 0, y: 20, scale: 0.9 }}
          animate={{ opacity: 1, y: 0, scale: 1 }}
          exit={{ opacity: 0, y: 20, scale: 0.9 }}
          className="fixed bottom-24 right-6 w-80 bg-white rounded-2xl shadow-2xl z-40 overflow-hidden"
        >
          <div className="bg-gradient-to-r from-primary-600 to-blue-600 text-white p-4">
            <h3 className="font-semibold">Chat with us!</h3>
            <p className="text-sm text-primary-100">We're here to help 24/7</p>
          </div>
          <div className="p-4 h-64 overflow-y-auto bg-gray-50">
            <div className="bg-white p-3 rounded-lg shadow-sm mb-3">
              <p className="text-sm">👋 Hi! How can we help you today?</p>
            </div>
          </div>
          <div className="p-4 border-t">
            <input
              type="text"
              placeholder="Type your message..."
              className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            />
          </div>
        </motion.div>
      )}
    </div>
  );
};

export default HomePage;
