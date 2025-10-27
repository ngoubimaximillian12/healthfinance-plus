import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { 
  UserIcon, 
  CalendarIcon, 
  ClipboardDocumentListIcon,
  ArrowRightOnRectangleIcon 
} from '@heroicons/react/24/outline';

const Header = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-white shadow-md sticky top-0 z-50">
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center py-4">
          {/* Logo */}
          <Link to="/" className="flex items-center space-x-2">
            <div className="bg-primary-600 text-white p-2 rounded-lg">
              <ClipboardDocumentListIcon className="h-8 w-8" />
            </div>
            <span className="text-2xl font-bold text-primary-600">
              HealthFinance<span className="text-primary-400">Plus</span>
            </span>
          </Link>

          {/* Navigation */}
          <nav className="hidden md:flex items-center space-x-8">
            {user ? (
              <>
                <Link to="/dashboard" className="text-gray-700 hover:text-primary-600 font-medium transition">
                  Dashboard
                </Link>
                <Link to="/appointments" className="text-gray-700 hover:text-primary-600 font-medium transition">
                  Appointments
                </Link>
                {user.roles?.includes('ADMIN') && (
                  <Link to="/admin" className="text-gray-700 hover:text-primary-600 font-medium transition">
                    Admin
                  </Link>
                )}
                <div className="flex items-center space-x-4">
                  <span className="text-gray-600 flex items-center">
                    <UserIcon className="h-5 w-5 mr-2" />
                    {user.email}
                  </span>
                  <button
                    onClick={handleLogout}
                    className="flex items-center text-red-600 hover:text-red-700 font-medium transition"
                  >
                    <ArrowRightOnRectangleIcon className="h-5 w-5 mr-1" />
                    Logout
                  </button>
                </div>
              </>
            ) : (
              <>
                <Link to="/" className="text-gray-700 hover:text-primary-600 font-medium transition">
                  Home
                </Link>
                <Link to="/about" className="text-gray-700 hover:text-primary-600 font-medium transition">
                  About
                </Link>
                <Link to="/services" className="text-gray-700 hover:text-primary-600 font-medium transition">
                  Services
                </Link>
                <Link to="/login" className="btn-secondary">
                  Login
                </Link>
                <Link to="/register" className="btn-primary">
                  Sign Up
                </Link>
              </>
            )}
          </nav>
        </div>
      </div>
    </header>
  );
};

export default Header;
