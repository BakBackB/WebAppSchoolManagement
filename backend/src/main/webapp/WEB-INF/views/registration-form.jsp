<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-store, must-revalidate">
        <title>Registration - School Management System</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="relative min-h-screen flex items-center justify-center p-4 font-sans antialiased text-gray-800 bg-slate-50 overflow-x-hidden overflow-y-auto">

        <%-- Gradient Background Decorative Blobs --%>
        <div class="absolute top-[-10%] left-[-10%] w-96 h-96 bg-purple-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60"></div>
        <div class="absolute top-[15%] right-[-5%] w-96 h-96 bg-cyan-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60"></div>
        <div class="absolute bottom-[-15%] left-[20%] w-[30rem] h-[30rem] bg-indigo-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60"></div>

        <%-- Login Card (Glassmorphism Effect) --%>
        <div class="relative z-10 bg-white/80 backdrop-blur-xl rounded-3xl shadow-2xl p-8 sm:p-10 w-full max-w-md border border-white/60">
            
            <%-- Header --%>
            <div class="mb-8 text-center">
                <div class="w-16 h-16 bg-gradient-to-br from-[#111827] to-gray-800 rounded-2xl mx-auto mb-4 flex items-center justify-center shadow-lg border border-gray-700">
                    <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 14l9-5-9-5-9 5 9 5z"></path>
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 14l6.16-3.422a12.083 12.083 0 01.665 6.479A11.952 11.952 0 0012 20.055a11.952 11.952 0 00-6.824-2.998 12.078 12.078 0 01.665-6.479L12 14z"></path>
                    </svg>
                </div>
                <h1 class="text-3xl font-bold tracking-tight text-gray-900 mb-1">Create Account</h1>
                <p class="text-sm text-gray-600 font-medium">School Management System</p>
            </div>

            <form action="register" method="post" autocomplete="off" class="space-y-5">
                <div>
                    <label for="role" class="block text-sm font-semibold text-gray-700 mb-1.5">Role</label>
                    <select id="role" name="role" 
                            class="w-full px-4 py-3 rounded-xl bg-white/60 border border-gray-200 text-gray-900 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all shadow-sm backdrop-blur-sm ${not empty errorRole ? 'border-red-500 bg-red-50' : ''}" required>
                        <option value="">Select Role</option>
                        <option value="2" ${role == 2 ? 'selected' : ''}>Teacher</option>    
                        <option value="3" ${role == 3 ? 'selected' : ''}>Student</option>
                    </select>
                <!-- Student Input Container -->
                <div id="student-section" style="display: none;">
                    <label for="studentCode" class="block text-sm font-semibold text-gray-700 mb-1.5">Student Code</label>
                    <input type="text" id="studentCode" name="studentCode" 
                    value="${studentCode}" placeholder="Enter Student Code"
                            class="w-full px-4 py-3 rounded-xl bg-white/60 border border-gray-200 text-gray-900 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all shadow-sm backdrop-blur-sm">
                    <c:if test="${not empty errorStudentCode}">
                        <span class="mt-1 text-sm text-red-600 block">${errorStudentCode}</span>
                    </c:if>    
                </div>
                <!-- Teacher Input Container -->
                <div id="teacher-section" style="display: none;">
                    <label for="teacherCode" class="block text-sm font-semibold text-gray-700 mb-1.5">Teacher Code:</label>
                    <input type="text" id="teacherCode" name="teacherCode" 
                    value="${teacherCode}" placeholder="Enter Teacher Code"
                            class="w-full px-4 py-3 rounded-xl bg-white/60 border border-gray-200 text-gray-900 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all shadow-sm backdrop-blur-sm">
                        <c:if test="${not empty errorTeacherCode}">
                            <span class="mt-1 text-sm text-red-600 block">${errorTeacherCode}</span>
                        </c:if>
                </div>
                    <c:if test="${not empty errorRole}">
                        <span class="mt-1 text-sm text-red-600 block">${errorRole}</span>
                    </c:if>
                <div>
                    <label for="username" class="block text-sm font-semibold text-gray-700 mb-1.5">Username</label>
                    <input type="text" id="username" name="username"
                           value="${username}" placeholder="Enter username"
                           class="w-full px-4 py-3 rounded-xl bg-white/60 border border-gray-200 text-gray-900 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all shadow-sm backdrop-blur-sm"
                            autofocus autocomplete="off">
                    <c:if test="${not empty errorUsername}">
                        <span class="mt-1 text-sm text-red-600 block">${errorUsername}</span>
                    </c:if>
                </div>

                <div>
                    <div class="flex justify-between items-center mb-1.5">
                        <label for="password" class="block text-sm font-semibold text-gray-700">Password</label>
                    </div>
                    <input type="password" id="password" name="password"
                           placeholder="Enter password"
                           class="w-full px-4 py-3 rounded-xl bg-white/60 border border-gray-200 text-gray-900 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all shadow-sm backdrop-blur-sm"
                           autocomplete="new-password">
                    <c:if test="${not empty errorPassword}">
                        <span class="mt-1 text-sm text-red-600 block">${errorPassword}</span>
                    </c:if>
                </div>

                <div>
                    <div class="flex justify-between items-center mb-1.5">
                        <label for="confirm" class="block text-sm font-semibold text-gray-700">Confirm Password</label>
                    </div>
                    <input type="password" id="confirm" name="confirm"
                           placeholder="Confirm password"
                           class="w-full px-4 py-3 rounded-xl bg-white/60 border border-gray-200 text-gray-900 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all shadow-sm backdrop-blur-sm"
                           autocomplete="new-password">
                    <c:if test="${not empty errorConfirm}">
                        <span class="mt-1 text-sm text-red-600 block">${errorConfirm}</span>
                    </c:if>
                </div>

                <div>
                    <label for="email" class="block text-sm font-semibold text-gray-700 mb-1.5">Email</label>
                    <input type="email" id="email" name="email"
                           value="${email}" placeholder="Enter email"
                           class="w-full px-4 py-3 rounded-xl bg-white/60 border border-gray-200 text-gray-900 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all shadow-sm backdrop-blur-sm"
                            autofocus autocomplete="off">
                    <c:if test="${not empty errorEmail}">
                        <span class="mt-1 text-sm text-red-600 block">${errorEmail}</span>
                    </c:if>
                </div>

                
                <div class="flex items-center">
                    <input type="checkbox" id="license" name="license" 
                           class="w-4 h-4 text-[#111827] bg-white border-gray-300 rounded focus:ring-[#111827] focus:ring-2 cursor-pointer transition-colors">
                    <label for="license" class="ml-2 text-sm font-medium text-gray-600 cursor-pointer select-none">I agree to the <a href="#" class="text-indigo-600 hover:underline">Terms and Conditions</a></label>
                </div>
                <button type="submit" class="w-full mt-4 bg-[#111827] hover:bg-gray-900 text-white text-sm font-bold py-3.5 rounded-xl transition-all shadow-lg hover:shadow-xl hover:-translate-y-0.5 flex justify-center items-center gap-2 duration-300">
                    Sign Up
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3"></path></svg>
                </button>
                <div class="text-center mt-4 text-sm">
                    Already have an account? <a href="login" class="text-indigo-600 hover:text-indigo-700 font-semibold ">Sign in</a>
                </div>
            </form>
            <div class="mt-8 bg-indigo-50/60 backdrop-blur-md rounded-2xl p-4 border border-indigo-100/50">
                <div class="flex items-center gap-2 mb-3 text-indigo-900 font-bold text-sm">
                    <svg class="w-4 h-4 text-indigo-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                    Test Credentials
                </div>
                <div class="space-y-2 text-sm">
                    <div class="flex justify-between items-center bg-white/80 p-2 rounded-lg border border-white shadow-sm">
                        <span class="text-gray-500 font-medium text-xs uppercase tracking-wider">Teacher</span>
                        <span class="font-mono text-indigo-700 bg-indigo-50 px-2.5 py-1 rounded-md font-medium text-xs"> TSE001/ teacher_phuc / 123teacherphuc* / phuc.le@school.edu.vn</span>
                    </div>
                    <div class="flex justify-between items-center bg-white/80 p-2 rounded-lg border border-white shadow-sm">
                        <span class="text-gray-500 font-medium text-xs uppercase tracking-wider">Student</span>
                        <span class="font-mono text-indigo-700 bg-indigo-50 px-2.5 py-1 rounded-md font-medium text-xs"> AI001/ student_huy / 123studenthuy* / huy.pham@student.edu.vn</span>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const roleSelect = document.getElementById('role');
            const studentSection = document.getElementById('student-section');
            const teacherSection = document.getElementById('teacher-section');

            function toggleInputs() {
                const selectedRole = roleSelect.value;

                // Hide both initially
                studentSection.style.display = 'none';
                teacherSection.style.display = 'none';

                // Show based on selection
                if (selectedRole === '3') {
                    studentSection.style.display = 'block';
                } else if (selectedRole === '2') {
                    teacherSection.style.display = 'block';
                }
            }

            // Listen for changes
            roleSelect.addEventListener('change', toggleInputs);

            // Run on page load to handle pre-selected values
            toggleInputs();
        });
    </script>
</html>