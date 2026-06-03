<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-store, must-revalidate">
        <title>Timetable Management</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    
    <body class="relative min-h-screen bg-slate-50 font-sans antialiased text-gray-800 overflow-x-hidden overflow-y-auto">
        
        <nav class="bg-white border-b border-gray-200 p-4 flex gap-8 justify-center shadow-sm relative z-20 w-full">
            <a href="${pageContext.request.contextPath}/payroll" 
               class="text-sm font-semibold text-gray-600 hover:text-gray-900 pb-1 transition-colors">
               Manage Payroll
            </a>
            <a href="${pageContext.request.contextPath}/subjects" 
               class="text-sm font-semibold text-gray-600 hover:text-gray-900 pb-1 transition-colors">
               Manage Subjects
            </a>
            <a href="${pageContext.request.contextPath}/schedules" 
               class="text-sm font-semibold text-blue-600 hover:text-blue-800 border-b-2 border-blue-600 pb-1 transition-colors">
               Manage Schedules
            </a>
        </nav>

        <%-- Shared Group Aesthetic Background Blobs --%>
        <div class="absolute top-[-10%] left-[-10%] w-96 h-96 bg-purple-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60 pointer-events-none"></div>
        <div class="absolute top-[15%] right-[-5%] w-96 h-96 bg-cyan-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60 pointer-events-none"></div>
        <div class="absolute bottom-[-15%] left-[20%] w-[30rem] h-[30rem] bg-indigo-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60 pointer-events-none"></div>

        <div class="flex items-center justify-center p-4 w-full min-h-[calc(100vh-73px)]">
            
            <%-- Core Glassmorphic Container --%>
            <div class="relative z-10 w-full max-w-4xl p-8 my-10 bg-white/80 backdrop-blur-xl rounded-3xl shadow-2xl border border-white/60">
                
                <%-- Module Header --%>
                <div class="bg-[#111827] rounded-2xl p-4 mb-6 shadow-xl text-white">
                    <div class="flex justify-between items-center px-2">
                        <h1 class="text-xl font-bold tracking-tight">Class Schedule & Timetable Management</h1>
                        <span class="bg-indigo-500/10 text-indigo-300 border border-indigo-500/20 px-3 py-1 rounded-lg text-xs font-medium">Scheduler Module</span>
                    </div>
                </div>

                <%-- Validation/Conflict Error Message Banner --%>
                <c:if test="${not empty errorMessage}">
                    <div class="bg-red-50/90 backdrop-blur-sm border border-red-200 text-red-600 px-4 py-3 rounded-xl mb-6 text-sm flex items-center gap-3 shadow-sm">
                        <svg class="w-5 h-5 text-red-500 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
                        </svg>
                        <span class="font-medium">${errorMessage}</span>
                    </div>
                </c:if>

                <%-- Quick Schedule Booking Form --%>
                <div class="mb-8 p-6 bg-white/50 border border-gray-100 rounded-2xl shadow-sm">
                    <h3 class="text-sm font-bold text-gray-900 mb-4 tracking-tight uppercase">Schedule New Time Block</h3>
                    <form method="post" action="${pageContext.request.contextPath}/schedules" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 items-end">
                        
                        <div>
                            <label for="classId" class="block text-xs font-semibold text-gray-600 mb-1">Target Class ID</label>
                            <input type="number" id="classId" name="classId" placeholder="e.g., 1"
                                   class="w-full px-3 py-2 rounded-xl bg-white/70 border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500" required>
                        </div>

                        <div>
                            <label for="subjectId" class="block text-xs font-semibold text-gray-600 mb-1">Subject ID</label>
                            <input type="number" id="subjectId" name="subjectId" placeholder="e.g., 4"
                                   class="w-full px-3 py-2 rounded-xl bg-white/70 border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500" required>
                        </div>

                        <div>
                            <label for="roomId" class="block text-xs font-semibold text-gray-600 mb-1">Physical Room ID</label>
                            <input type="number" id="roomId" name="roomId" placeholder="e.g., 2"
                                   class="w-full px-3 py-2 rounded-xl bg-white/70 border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500" required>
                        </div>

                        <div>
                            <label for="dayOfWeek" class="block text-xs font-semibold text-gray-600 mb-1">Day of Week Rule</label>
                            <select id="dayOfWeek" name="dayOfWeek" 
                                    class="w-full px-3 py-2 rounded-xl bg-white/70 border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500" required>
                                <option value="MONDAY">Monday</option>
                                <option value="TUESDAY">Tuesday</option>
                                <option value="WEDNESDAY">Wednesday</option>
                                <option value="THURSDAY">Thursday</option>
                                <option value="FRIDAY">Friday</option>
                                <option value="SATURDAY">Saturday</option>
                            </select>
                        </div>

                        <div>
                            <label for="startTime" class="block text-xs font-semibold text-gray-600 mb-1">Start Time</label>
                            <input type="time" id="startTime" name="startTime"
                                   class="w-full px-3 py-2 rounded-xl bg-white/70 border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500" required>
                        </div>

                        <div>
                            <label for="endTime" class="block text-xs font-semibold text-gray-600 mb-1">End Time</label>
                            <input type="time" id="endTime" name="endTime"
                                   class="w-full px-3 py-2 rounded-xl bg-white/70 border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500" required>
                        </div>

                        <div class="sm:col-span-2 md:col-span-3 flex justify-end pt-2">
                            <button type="submit" class="w-full sm:w-auto bg-[#111827] hover:bg-gray-900 text-white text-sm font-bold py-2.5 px-8 rounded-xl transition-all shadow-md duration-200">
                                Commit to Timetable
                            </button>
                        </div>
                    </form>
                </div>

                <%-- Timetable Records Grid Layout --%>
                <div class="overflow-x-auto border border-gray-100 rounded-2xl shadow-inner bg-white/50">
                    <table class="w-full text-left border-collapse">
                        <thead>
                            <tr class="bg-gray-100/70 border-b border-gray-200">
                                <th class="px-4 py-3 text-xs font-bold uppercase tracking-wider text-gray-500 w-16">ID</th>
                                <th class="px-4 py-3 text-xs font-bold uppercase tracking-wider text-gray-500">Class</th>
                                <th class="px-4 py-3 text-xs font-bold uppercase tracking-wider text-gray-500">Subject</th>
                                <th class="px-4 py-3 text-xs font-bold uppercase tracking-wider text-gray-500">Room Location</th>
                                <th class="px-4 py-3 text-xs font-bold uppercase tracking-wider text-gray-500">Day</th>
                                <th class="px-4 py-3 text-xs font-bold uppercase tracking-wider text-gray-500">Duration Block</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-100 text-sm">
                            <c:choose>
                                <c:when test="${not empty schedules}">
                                    <c:forEach var="sch" items="${schedules}">
                                        <tr class="hover:bg-white/40 transition-colors">
                                            <td class="px-4 py-3.5 font-mono text-xs text-gray-400"><c:out value="${sch.scheduleId}"/></td>
                                            <td class="px-4 py-3.5 font-medium text-gray-700">Class Ref: <c:out value="${sch.classId}"/></td>
                                            <td class="px-4 py-3.5 font-semibold text-gray-900">Subject Ref: <c:out value="${sch.subjectId}"/></td>
                                            <td class="px-4 py-3.5 text-gray-600"><span class="bg-slate-200/60 px-2 py-0.5 rounded text-xs font-mono">Room ID: <c:out value="${sch.roomId}"/></span></td>
                                            <td class="px-4 py-3.5"><span class="text-xs font-bold px-2 py-1 rounded-lg bg-indigo-50 text-indigo-700"><c:out value="${sch.dayOfWeek}"/></span></td>
                                            <td class="px-4 py-3.5 font-mono text-xs font-medium text-gray-500"><c:out value="${sch.startTime}"/> - <c:out value="${sch.endTime}"/></td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" class="px-6 py-12 text-center text-sm text-gray-400 font-medium">No system classes scheduled in current timetable matrix grid.</td>
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