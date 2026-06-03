<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Portal - System Schedule Manager</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-[#f3f4f6] font-sans antialiased text-gray-800">
        
       <nav class="bg-white border-b border-gray-200 p-4 flex gap-8 justify-center shadow-sm relative z-20 w-full">
    <a href="${pageContext.request.contextPath}/payroll" 
       class="text-sm font-semibold text-gray-600 hover:text-gray-900 pb-1 transition-colors">
       Manage Payroll
    </a>
    <a href="${pageContext.request.contextPath}/admin/schedule-dashboard" 
       class="text-sm font-semibold text-blue-600 hover:text-blue-800 border-b-2 border-blue-600 pb-1 transition-colors">
       Manage Schedules
    </a>
</nav>

        <div class="p-8 max-w-6xl mx-auto space-y-8">
            
            <div class="bg-white rounded-3xl p-6 shadow-xl border border-gray-100">
                <div class="bg-[#111827] rounded-2xl p-4 mb-6 text-white flex justify-between items-center">
                    <div>
                        <h2 class="text-lg font-bold tracking-tight">Deploy New Open Course Slot</h2>
                        <p class="text-xs text-gray-400 mt-0.5">Input existing structural reference entity ID parameters to setup a time block</p>
                    </div>
                    <span class="bg-blue-500/20 text-blue-400 border border-blue-500/30 px-3 py-1 rounded-lg text-xs font-medium">Data Entry</span>
                </div>

                <c:if test="${not empty sessionScope.dbError}">
                    <div class="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-xl mb-5 text-sm font-medium shadow-sm">
                        ${sessionScope.dbError}
                        <c:remove var="dbError" scope="session"/>
                    </div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/admin/schedule-dashboard" class="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
    <div>
        <label class="block mb-1 text-xs font-bold text-gray-700 uppercase">Subject Name</label>
        <input type="text" name="subjectName" placeholder="e.g., Database Structures Algorithms" class="bg-gray-50 border border-gray-300 text-sm rounded-xl block w-full p-2.5 outline-none focus:ring-2 focus:ring-blue-500" required>
    </div>
    <div>
        <label class="block mb-1 text-xs font-bold text-gray-700 uppercase">Class Code</label>
        <input type="text" name="className" placeholder="e.g., IT1234" class="bg-gray-50 border border-gray-300 text-sm rounded-xl block w-full p-2.5 outline-none focus:ring-2 focus:ring-blue-500" required>
    </div>
    <div>
        <label class="block mb-1 text-xs font-bold text-gray-700 uppercase">Section</label>
        <input type="text" name="section" placeholder="e.g., A" class="bg-gray-50 border border-gray-300 text-sm rounded-xl block w-full p-2.5 outline-none focus:ring-2 focus:ring-blue-500" required>
    </div>
    <div>
        <label class="block mb-1 text-xs font-bold text-gray-700 uppercase">Room/Room Location</label>
        <input type="text" name="roomName" placeholder="e.g., Lab 302" class="bg-gray-50 border border-gray-300 text-sm rounded-xl block w-full p-2.5 outline-none focus:ring-2 focus:ring-blue-500" required>
    </div>
    <div>
        <label class="block mb-1 text-xs font-bold text-gray-700 uppercase">Day of Week</label>
        <select name="dayOfWeek" class="bg-gray-50 border border-gray-300 text-sm rounded-xl block w-full p-2.5 outline-none focus:ring-2 focus:ring-blue-500" required>
            <option value="MONDAY">Monday</option>
            <option value="TUESDAY">Tuesday</option>
            <option value="WEDNESDAY">Wednesday</option>
            <option value="THURSDAY">Thursday</option>
            <option value="FRIDAY">Friday</option>
            <option value="SATURDAY">Saturday</option>
        </select>
    </div>
    <div>
        <label class="block mb-1 text-xs font-bold text-gray-700 uppercase">Start Time</label>
        <input type="time" name="startTime" class="bg-gray-50 border border-gray-300 text-sm rounded-xl block w-full p-2.5 outline-none focus:ring-2 focus:ring-blue-500" required>
    </div>
    <div>
        <label class="block mb-1 text-xs font-bold text-gray-700 uppercase">End Time</label>
        <input type="time" name="endTime" class="bg-gray-50 border border-gray-300 text-sm rounded-xl block w-full p-2.5 outline-none focus:ring-2 focus:ring-blue-500" required>
    </div>
    <div>
        <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white text-sm font-bold py-2.5 px-6 rounded-xl transition-all shadow-md w-full">
            Publish Open Course Slot ↗
        </button>
    </div>
</form>
            </div>

            <div class="bg-white/80 backdrop-blur-xl rounded-3xl p-8 shadow-2xl border border-white/60">
                <div class="border-b border-gray-100 pb-4 mb-6">
                    <h2 class="text-xl font-bold tracking-tight text-gray-900">Master Institutional Schedule Matrix</h2>
                    <p class="text-xs text-gray-400 mt-0.5">Global real-time overview of all current courses, timeline blocks, and active assignments</p>
                </div>

                <div class="overflow-hidden border border-gray-100 rounded-2xl shadow-inner bg-white/50">
                    <table class="w-full text-left border-collapse text-sm">
                        <thead>
    <tr class="bg-gray-100/80 border-b border-gray-200 text-gray-600 font-bold">
        <th class="px-6 py-4 w-16">ID</th>
        <th class="px-6 py-4">Subject Name</th>
        <th class="px-6 py-4">Target Class</th>
        <th class="px-6 py-4">Room Location</th>
        <th class="px-6 py-4">Day & Time Slot</th>
        <th class="px-6 py-4">Assigned Faculty</th>
        <th class="px-6 py-4 text-center">Actions</th> </tr>
</thead>
                        <tbody class="divide-y divide-gray-100 font-medium">
                            <c:choose>
                                <c:when test="${not empty scheduleRows}">
                                    <c:forEach var="row" items="${scheduleRows}">
                                        <tr class="hover:bg-white/60 transition-colors">
    <td class="px-6 py-4 font-mono text-gray-400 text-xs">#${row.id}</td>
    <td class="px-6 py-4 font-semibold text-gray-900">${row.subjectName}</td>
    <td class="px-6 py-4 text-gray-700">${row.className}</td>
    <td class="px-6 py-4 text-gray-600">
        <span class="bg-gray-100 text-gray-800 text-[11px] px-2.5 py-1 rounded-md font-mono">${row.roomName}</span>
    </td>
    <td class="px-6 py-4 text-xs font-mono text-indigo-600">
        <span class="bg-indigo-50 text-indigo-700 px-2 py-0.5 rounded text-[10px] font-bold mr-1.5">${row.day}</span>
        ${row.time}
    </td>
    <td class="px-6 py-4">
        <span class="${row.teacherName.contains('🔴') ? 'text-rose-500 font-medium text-xs' : 'text-emerald-600 font-bold'}">
            ${row.teacherName}
        </span>
    </td>
    <td class="px-6 py-4 text-center">
        <form method="post" action="${pageContext.request.contextPath}/admin/schedule-dashboard" class="m-0" onsubmit="return confirm('Are you sure you want to completely delete this course schedule block?');">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="scheduleId" value="${row.id}">
            <button type="submit" class="bg-rose-500 hover:bg-rose-600 text-white text-xs font-bold py-1.5 px-4 rounded-xl transition-all shadow-sm">
                Remove
            </button>
        </form>
    </td>
</tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" class="px-6 py-12 text-center text-sm text-gray-400 font-semibold">
                                            Empty Set: No schedules currently initialized in the database.
                                        </td>
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