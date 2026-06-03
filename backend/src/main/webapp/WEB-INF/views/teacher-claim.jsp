<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Teacher Portal - Schedule Management</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    
    <body class="relative min-h-screen bg-slate-50 font-sans antialiased text-gray-800 overflow-x-hidden overflow-y-auto">
        
        <nav class="bg-white border-b border-gray-200 p-4 flex gap-8 justify-center shadow-sm relative z-20 w-full">
            <a href="<c:url value='/financial-statistics'/>" class="text-sm font-semibold text-gray-600 hover:text-gray-900 pb-1 transition-colors">Financial Statistics</a>
            <a href="<c:url value='/teacher/claim-classes'/>" class="text-sm font-semibold text-blue-600 hover:text-blue-800 border-b-2 border-blue-600 pb-1 transition-colors">Claim Class Slots</a>
        </nav>

        <div class="absolute top-[-10%] left-[-10%] w-96 h-96 bg-purple-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60 pointer-events-none"></div>
        <div class="absolute top-[15%] right-[-5%] w-96 h-96 bg-cyan-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60 pointer-events-none"></div>

        <div class="flex flex-col items-center justify-start p-6 w-full gap-8 relative z-10 mt-6">
            
            <div class="w-full max-w-4xl p-8 bg-white/80 backdrop-blur-xl rounded-3xl shadow-2xl border border-white/60">
                <div class="bg-[#111827] rounded-2xl p-4 mb-6 shadow-xl text-white flex justify-between items-center">
                    <div>
                        <h1 class="text-xl font-bold tracking-tight">Open Class Section Schedule Postings</h1>
                        <p class="text-xs text-gray-400 mt-0.5">Review system schedule blocks below with no active faculty coverage assigned</p>
                    </div>
                    <span class="bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 px-3 py-1 rounded-lg text-xs font-medium">Available</span>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div class="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-xl mb-5 text-sm font-medium shadow-sm">
                        ${errorMessage}
                    </div>
                </c:if>

                <div class="overflow-hidden border border-gray-100 rounded-2xl shadow-inner bg-white/50">
                    <table class="w-full text-left border-collapse text-sm">
                        <thead>
                            <tr class="bg-gray-100/70 border-b border-gray-200 text-gray-600 font-bold">
                                <th class="px-6 py-3">Class Target</th>
                                <th class="px-6 py-3">Subject ID</th>
                                <th class="px-6 py-3">Subject Name</th>
                                <th class="px-6 py-3">Day & Time Duration Block</th>
                                <th class="px-6 py-3 text-center">Operation Action</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-100 font-medium">
                            <c:choose>
                                <c:when test="${not empty openSchedules}">
                                    <c:forEach var="sch" items="${openSchedules}">
                                        <tr class="hover:bg-white/40 transition-colors">
                                            <td class="px-6 py-4 text-gray-900">Class Section #${sch.classId}</td>
                                            <td class="px-6 py-4 font-semibold text-indigo-600">Subject ID #${sch.subjectId}</td>
                                            <td class="px-6 py-4 font-semibold text-indigo-600">${sch.subjectName}</td>
                                            <td class="px-6 py-4 text-xs font-mono text-gray-600">
                                                <span class="bg-indigo-50 text-indigo-700 px-2 py-0.5 rounded text-[10px] font-bold mr-1">${sch.dayOfWeek}</span>
                                                ${sch.startTime} - ${sch.endTime} (Room ${sch.roomId})
                                            </td>
                                            <td class="px-6 py-4 text-center">
                                                <form method="post" action="${pageContext.request.contextPath}/teacher/claim-classes" class="m-0">
                                                    <input type="hidden" name="action" value="claim">
                                                    <input type="hidden" name="classId" value="${sch.classId}">
                                                    <input type="hidden" name="subjectId" value="${sch.subjectId}">
                                                    <button type="submit" class="bg-emerald-600 hover:bg-emerald-700 text-white text-xs font-bold py-2 px-4 rounded-xl transition-all shadow-sm">
                                                        Claim Section
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="4" class="px-6 py-8 text-center text-sm text-gray-400 font-semibold">
                                            🎉 All system schedule blocks currently have teacher coverage assigned.
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="w-full max-w-4xl p-8 bg-white/80 backdrop-blur-xl rounded-3xl shadow-2xl border border-white/60">
                <div class="bg-indigo-950 rounded-2xl p-4 mb-6 shadow-xl text-white flex justify-between items-center">
                    <div>
                        <h1 class="text-xl font-bold tracking-tight">Your Confirmed Teaching Assignments</h1>
                        <p class="text-xs text-indigo-300 mt-0.5">Classes you have successfully claimed for your semester schedule</p>
                    </div>
                    <span class="bg-indigo-500/20 text-indigo-300 border border-indigo-500/30 px-3 py-1 rounded-lg text-xs font-medium">My Schedule</span>
                </div>

                <div class="overflow-hidden border border-gray-100 rounded-2xl shadow-inner bg-white/50">
                    <table class="w-full text-left border-collapse text-sm">
                        <thead>
                            <tr class="bg-indigo-50/50 border-b border-gray-200 text-gray-600 font-bold">
                                <th class="px-6 py-3">Class Target</th>
                                <th class="px-6 py-3">Subject ID</th>
                                <th class="px-6 py-3">Subject Name</th>
                                <th class="px-6 py-3">Day & Time Duration Block</th>
                                <th class="px-6 py-3 text-center">Operation Action</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-100 font-medium">
                            <c:choose>
                                <c:when test="${not empty mySchedules}">
                                    <c:forEach var="mySch" items="${mySchedules}">
                                        <tr class="bg-indigo-50/10 hover:bg-white/40 transition-colors">
                                            <td class="px-6 py-4 text-gray-900">Class Section #${mySch.classId}</td>
                                            <td class="px-6 py-4 font-semibold text-indigo-600">Subject ID #${mySch.subjectId}</td>
                                            <td class="px-6 py-4 font-semibold text-indigo-600">${mySch.subjectName}</td>
                                            <td class="px-6 py-4 text-xs font-mono text-gray-600">
                                                <span class="bg-indigo-600 text-white px-2 py-0.5 rounded text-[10px] font-bold mr-1">${mySch.dayOfWeek}</span>
                                                ${mySch.startTime} - ${mySch.endTime} (Room ${mySch.roomId})
                                            </td>
                                            <td class="px-6 py-4 text-center">
                                                <form method="post" action="${pageContext.request.contextPath}/teacher/claim-classes" class="m-0">
                                                    <input type="hidden" name="action" value="drop">
                                                    <input type="hidden" name="classId" value="${mySch.classId}">
                                                    <input type="hidden" name="subjectId" value="${mySch.subjectId}">
                                                    <button type="submit" class="bg-rose-500 hover:bg-rose-600 text-white text-xs font-bold py-2 px-4 rounded-xl transition-all shadow-sm">
                                                        Drop Class
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="4" class="px-6 py-8 text-center text-sm text-gray-400 font-semibold">
                                            You haven't claimed any classes yet. Choose a slot from the table above!
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