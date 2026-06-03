<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-store, must-revalidate">
        <title>Curriculum Management</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    
    <body class="relative min-h-screen bg-slate-50 font-sans antialiased text-gray-800 overflow-x-hidden overflow-y-auto">
        
        <nav class="bg-white border-b border-gray-200 p-4 flex gap-8 justify-center shadow-sm relative z-20 w-full">
    <a href="${pageContext.request.contextPath}/payroll" 
       class="text-sm font-semibold text-gray-600 hover:text-gray-900 pb-1 transition-colors">
       Manage Payroll
    </a>
    <a href="${pageContext.request.contextPath}/subjects" 
       class="text-sm font-semibold text-blue-600 hover:text-blue-800 border-b-2 border-blue-600 pb-1 transition-colors">
       Manage Subjects
    </a>
    <a href="${pageContext.request.contextPath}/schedules" 
       class="text-sm font-semibold text-gray-600 hover:text-gray-900 pb-1 transition-colors">
       Manage Schedules
    </a>
</nav>

        <%-- Gradient Background Decorative Blobs --%>
        <div class="absolute top-[-10%] left-[-10%] w-96 h-96 bg-purple-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60 pointer-events-none"></div>
        <div class="absolute top-[15%] right-[-5%] w-96 h-96 bg-cyan-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60 pointer-events-none"></div>
        <div class="absolute bottom-[-15%] left-[20%] w-[30rem] h-[30rem] bg-indigo-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60 pointer-events-none"></div>

        <div class="flex items-center justify-center p-4 w-full min-h-[calc(100vh-73px)]">
            <div class="relative z-10 w-full max-w-2xl p-8 bg-white/80 backdrop-blur-xl rounded-3xl shadow-2xl border border-white/60">
                
                <div class="bg-[#111827] rounded-2xl p-4 mb-6 shadow-xl text-white">
                    <div class="flex justify-between items-center px-2">
                        <h1 class="text-xl font-bold tracking-tight">Curriculum Subject Management</h1>
                        <span class="bg-indigo-500/10 text-indigo-300 border border-indigo-500/20 px-3 py-1 rounded-lg text-xs font-medium">Academic Module</span>
                    </div>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div class="bg-red-50/90 backdrop-blur-sm border border-red-200 text-red-600 px-4 py-3 rounded-xl mb-5 text-sm flex items-center gap-3 shadow-sm">
                        <span class="font-medium">${errorMessage}</span>
                    </div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/subjects" class="mb-8 p-4 bg-white/50 border border-gray-100 rounded-2xl flex items-end gap-4">
                    <div class="flex-1">
                        <label for="subjectName" class="block text-sm font-semibold text-gray-700 mb-1.5">New Subject Name</label>
                        <input type="text" id="subjectName" name="subjectName" placeholder="e.g., Object-Oriented Programming"
                               class="w-full px-4 py-2 rounded-xl bg-white/60 border border-gray-200 text-gray-900 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all text-sm" required>
                    </div>
                    <button type="submit" class="bg-[#111827] hover:bg-gray-900 text-white text-sm font-bold py-2 px-6 rounded-xl h-[38px] transition-all duration-200 shadow-sm">
                        Add Course
                    </button>
                </form>

                <div class="overflow-hidden border border-gray-100 rounded-2xl shadow-inner bg-white/50">
                    <table class="w-full text-left border-collapse">
                        <thead>
                            <tr class="bg-gray-100/70 border-b border-gray-200">
                                <th class="px-6 py-3 text-xs font-bold uppercase tracking-wider text-gray-500 w-24">ID</th>
                                <th class="px-6 py-3 text-xs font-bold uppercase tracking-wider text-gray-500">Subject Name</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-100">
                            <c:choose>
                                <c:when test="${not empty subjects}">
                                    <c:forEach var="sub" items="${subjects}">
                                        <tr class="hover:bg-white/40 transition-colors">
                                            <td class="px-6 py-3.5 font-mono text-xs text-gray-400"><c:out value="${sub.subjectId}"/></td>
                                            <td class="px-6 py-3.5 font-semibold text-gray-900"><c:out value="${sub.subjectName}"/></td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="2" class="px-6 py-10 text-center text-sm text-gray-400 font-medium">No system subjects registered.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>