import React from 'react';
import { Link } from 'react-router-dom';
import { 
  EnvelopeIcon, 
  PhoneIcon, 
  MapPinIcon,
  ClipboardDocumentListIcon 
} from '@heroicons/react/24/outline';

const Footer = () => {
  return (
    <footer className="bg-gray-900 text-gray-300">
      <div className="container mx-auto px-4 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Company Info */}
          <div>
            <div className="flex items-center space-x-2 mb-4">
              <div className="bg-primary-600 text-white p-2 rounded-lg">
                <ClipboardDocumentListIcon className="h-6 w-6" />
              </div>
              <span className="text-xl font-bold text-white">
                HealthFinance<span className="text-primary-400">Plus</span>
              </span>
            </div>
            <p className="text-sm">
              Your trusted partner in healthcare management. We provide comprehensive 
              solutions for appointments, medical records, and insurance management.
            </p>
          </div>

          {/* Quick Links */}
          <div>
            <h3 className="text-white font-semibold mb-4">Quick Links</h3>
            <ul className="space-y-2">
              <li>
                <Link to="/" className="hover:text-primary-400 transition">Home</Link>
              </li>
              <li>
                <Link to="/about" className="hover:text-primary-400 transition">About Us</Link>
              </li>
              <li>
                <Link to="/services" className="hover:text-primary-400 transition">Services</Link>
              </li>
              <li>
                <Link to="/appointments" className="hover:text-primary-400 transition">Book Appointment</Link>
              </li>
            </ul>
          </div>

          {/* Services */}
          <div>
            <h3 className="text-white font-semibold mb-4">Our Services</h3>
            <ul className="space-y-2">
              <li className="hover:text-primary-400 transition cursor-pointer">Appointment Booking</li>
              <li className="hover:text-primary-400 transition cursor-pointer">Medical Records</li>
              <li className="hover:text-primary-400 transition cursor-pointer">Insurance Claims</li>
              <li className="hover:text-primary-400 transition cursor-pointer">Prescription Management</li>
              <li className="hover:text-primary-400 transition cursor-pointer">Billing Services</li>
            </ul>
          </div>

          {/* Contact */}
          <div>
            <h3 className="text-white font-semibold mb-4">Contact Us</h3>
            <ul className="space-y-3">
              <li className="flex items-center space-x-2">
                <PhoneIcon className="h-5 w-5 text-primary-400" />
                <span>+1 (555) 123-4567</span>
              </li>
              <li className="flex items-center space-x-2">
                <EnvelopeIcon className="h-5 w-5 text-primary-400" />
                <span>info@healthfinanceplus.com</span>
              </li>
              <li className="flex items-start space-x-2">
                <MapPinIcon className="h-5 w-5 text-primary-400 mt-1" />
                <span>123 Healthcare Ave, Medical District, NY 10001</span>
              </li>
            </ul>
          </div>
        </div>

        {/* Bottom Bar */}
        <div className="border-t border-gray-800 mt-8 pt-8 text-center text-sm">
          <p>&copy; {new Date().getFullYear()} HealthFinancePlus. All rights reserved.</p>
          <div className="mt-2 space-x-4">
            <Link to="/privacy" className="hover:text-primary-400 transition">Privacy Policy</Link>
            <span>•</span>
            <Link to="/terms" className="hover:text-primary-400 transition">Terms of Service</Link>
            <span>•</span>
            <Link to="/contact" className="hover:text-primary-400 transition">Contact</Link>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
