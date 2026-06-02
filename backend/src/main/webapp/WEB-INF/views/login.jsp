<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-store, must-revalidate">
        <title>Login - School Management System</title>
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
                <h1 class="text-3xl font-bold tracking-tight text-gray-900 mb-1">Welcome Back</h1>
                <p class="text-sm text-gray-600 font-medium">School Management System</p>
            </div>

            <%-- Error from failed login attempt --%>
            <c:if test="${not empty error}">
                <div class="bg-red-50/90 backdrop-blur-sm border border-red-200 text-red-600 px-4 py-3 rounded-xl mb-6 text-sm flex items-center gap-3 shadow-sm" role="alert">
                    <svg class="w-5 h-5 text-red-500 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20"><path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path></svg>
                    <span class="font-medium">${error}</span>
                </div>
            </c:if>

            <%-- Success message from logout --%>
            <c:if test="${not empty param.message}">
                <div class="bg-emerald-50/90 backdrop-blur-sm border border-emerald-200 text-emerald-600 px-4 py-3 rounded-xl mb-6 text-sm flex items-center gap-3 shadow-sm" role="alert">
                    <svg class="w-5 h-5 text-emerald-500 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20"><path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"></path></svg>
                    <span class="font-medium">You have been logged out successfully.</span>
                </div>
            </c:if>

            <form action="login" method="post" autocomplete="off" class="space-y-5">
                <div>
                    <label for="username" class="block text-sm font-semibold text-gray-700 mb-1.5">Username</label>
                    <input type="text" id="username" name="username"
                           value="${username}" placeholder="Enter username"
                           class="w-full px-4 py-3 rounded-xl bg-white/60 border border-gray-200 text-gray-900 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all shadow-sm backdrop-blur-sm"
                            autofocus autocomplete="off">
                </div>

                <div>
                    <div class="flex justify-between items-center mb-1.5">
                        <label for="password" class="block text-sm font-semibold text-gray-700">Password</label>
                        <a href="#" class="text-xs font-semibold text-indigo-600 hover:text-indigo-700 transition-colors">Forgot password?</a>
                    </div>
                    <input type="password" id="password" name="password"
                           placeholder="Enter password"
                           class="w-full px-4 py-3 rounded-xl bg-white/60 border border-gray-200 text-gray-900 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all shadow-sm backdrop-blur-sm"
                           autocomplete="new-password">
                </div>

                <div class="flex items-center">
                    <input type="checkbox" id="remember" name="remember" 
                           class="w-4 h-4 text-[#111827] bg-white border-gray-300 rounded focus:ring-[#111827] focus:ring-2 cursor-pointer transition-colors">
                    <label for="remember" class="ml-2 text-sm font-medium text-gray-600 cursor-pointer select-none">Remember me</label>
                </div>

                <button type="submit" class="w-full mt-4 bg-[#111827] hover:bg-gray-900 text-white text-sm font-bold py-3.5 rounded-xl transition-all shadow-lg hover:shadow-xl hover:-translate-y-0.5 flex justify-center items-center gap-2 duration-300">
                    Sign In
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3"></path></svg>
                </button>
            </form>

            <%-- Demo Credentials Box --%>
            <div class="mt-8 bg-indigo-50/60 backdrop-blur-md rounded-2xl p-4 border border-indigo-100/50">
                <div class="flex items-center gap-2 mb-3 text-indigo-900 font-bold text-sm">
                    <svg class="w-4 h-4 text-indigo-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                    Test Credentials
                </div>
                <div class="space-y-2 text-sm">
                    <div class="flex justify-between items-center bg-white/80 p-2 rounded-lg border border-white shadow-sm">
                        <span class="text-gray-500 font-medium text-xs uppercase tracking-wider">Admin</span>
                        <span class="font-mono text-indigo-700 bg-indigo-50 px-2.5 py-1 rounded-md font-medium text-xs">admin / 123admin</span>
                    </div>
                    <div class="flex justify-between items-center bg-white/80 p-2 rounded-lg border border-white shadow-sm">
                        <span class="text-gray-500 font-medium text-xs uppercase tracking-wider">Teacher</span>
                        <span class="font-mono text-indigo-700 bg-indigo-50 px-2.5 py-1 rounded-md font-medium text-xs">teacher_minh / 123teacher</span>
                    </div>
                    <div class="flex justify-between items-center bg-white/80 p-2 rounded-lg border border-white shadow-sm">
                        <span class="text-gray-500 font-medium text-xs uppercase tracking-wider">Student</span>
                        <span class="font-mono text-indigo-700 bg-indigo-50 px-2.5 py-1 rounded-md font-medium text-xs">student_bao / 123student</span>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>