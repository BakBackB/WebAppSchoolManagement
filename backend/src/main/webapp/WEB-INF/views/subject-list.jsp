<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-store, must-revalidate">
        <title>Curriculum Management & Timetable</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    
    <body class="relative min-h-screen bg-slate-50 font-sans antialiased text-gray-800 overflow-x-hidden overflow-y-auto">
      <nav class="bg-white border-b border-gray-200 p-4 flex gap-8 justify-center shadow-sm relative z-20 w-full">
    <a href="<c:url value='/payment'/>" 
       class="text-sm font-semibold text-gray-600 hover:text-gray-900 pb-1 transition-colors">
       Fee Payment
    </a>
    <a href="<c:url value='/subjects'/>" 
       class="text-sm font-semibold text-blue-600 hover:text-blue-800 border-b-2 border-blue-600 pb-1 transition-colors">
       Manage Subjects
    </a>
</nav>
       
        <%-- Gradient Background Decorative Blobs --%>
        <div class="absolute top-[-10%] left-[-10%] w-96 h-96 bg-purple-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60 pointer-events-none"></div>
        <div class="absolute top-[15%] right-[-5%] w-96 h-96 bg-cyan-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60 pointer-events-none"></div>
        <div class="absolute bottom-[-15%] left-[20%] w-[30rem] h-[30rem] bg-indigo-300 rounded-full mix-blend-multiply filter blur-[100px] opacity-60 pointer-events-none"></div>

        <div class="flex flex-col items-center justify-start p-6 w-full gap-8 relative z-10">
            
            <div class="w-full max-w-4xl p-8 bg-white/80 backdrop-blur-xl rounded-3xl shadow-2xl border border-white/60">
                <div class="bg-[#111827] rounded-2xl p-4 mb-6 shadow-xl text-white flex justify-between items-center">
                    <div>
                        <h1 class="text-xl font-bold tracking-tight">Available Curriculum Offerings</h1>
                        <p class="text-xs text-gray-400 mt-0.5">Select a pre-configured course block to register it into the schedule matrix</p>
                    </div>
                    <span class="bg-indigo-500/10 text-indigo-300 border border-indigo-500/20 px-3 py-1 rounded-lg text-xs font-medium">Registration Module</span>
                </div>

                <div class="overflow-hidden border border-gray-100 rounded-2xl shadow-inner bg-white/50">
                    <table class="w-full text-left border-collapse text-sm">
                        <thead>
                            <tr class="bg-gray-100/70 border-b border-gray-200 text-gray-600 font-bold">
                                <th class="px-6 py-3">Subject Name</th>
                                <th class="px-6 py-3">Assigned Teacher</th>
                                <th class="px-6 py-3">Day & Time Block</th>
                                <th class="px-6 py-3 text-center">Action</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-100 font-medium">
                            <tr class="hover:bg-white/40 transition-colors">
                                <td class="px-6 py-4 font-semibold text-gray-900">Data Structures</td>
                                <td class="px-6 py-4 text-gray-600">Prof. Thomas</td>
                                <td class="px-6 py-4 text-xs font-mono text-indigo-600 font-bold">MONDAY | 08:00 - 10:00</td>
                                <td class="px-6 py-4 text-center">
                                    <button onclick="addCourseToTimetable('Data Structures', 'Prof. Thomas', 'MONDAY', '08:00', '10:00')" class="bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold py-2 px-4 rounded-xl transition-all shadow-sm">Select Course</button>
                                </td>
                            </tr>
                            <tr class="hover:bg-white/40 transition-colors">
                                <td class="px-6 py-4 font-semibold text-gray-900">Database Systems</td>
                                <td class="px-6 py-4 text-gray-600">Prof. Jenkins</td>
                                <td class="px-6 py-4 text-xs font-mono text-indigo-600 font-bold">TUESDAY | 10:00 - 12:00</td>
                                <td class="px-6 py-4 text-center">
                                    <button onclick="addCourseToTimetable('Database Systems', 'Prof. Jenkins', 'TUESDAY', '10:00', '12:00')" class="bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold py-2 px-4 rounded-xl transition-all shadow-sm">Select Course</button>
                                </td>
                            </tr>
                            <tr class="hover:bg-white/40 transition-colors">
                                <td class="px-6 py-4 font-semibold text-gray-900">Java Web Development</td>
                                <td class="px-6 py-4 text-gray-600">Prof. Albus</td>
                                <td class="px-6 py-4 text-xs font-mono text-indigo-600 font-bold">MONDAY | 09:00 - 11:00</td>
                                <td class="px-6 py-4 text-center">
                                    <button onclick="addCourseToTimetable('Java Web Development', 'Prof. Albus', 'MONDAY', '09:00', '11:00')" class="bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold py-2 px-4 rounded-xl transition-all shadow-sm">Select Course</button>
                                </td>
                            </tr>
                            <tr class="hover:bg-white/40 transition-colors">
                                <td class="px-6 py-4 font-semibold text-gray-900">Operating Systems</td>
                                <td class="px-6 py-4 text-gray-600">Prof. Snape</td>
                                <td class="px-6 py-4 text-xs font-mono text-indigo-600 font-bold">WEDNESDAY | 14:00 - 16:00</td>
                                <td class="px-6 py-4 text-center">
                                    <button onclick="addCourseToTimetable('Operating Systems', 'Prof. Snape', 'WEDNESDAY', '14:00', '16:00')" class="bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold py-2 px-4 rounded-xl transition-all shadow-sm">Select Course</button>
                                </td>
                            </tr>
                            <tr class="hover:bg-white/40 transition-colors">
                                <td class="px-6 py-4 font-semibold text-gray-900">Software Engineering</td>
                                <td class="px-6 py-4 text-gray-600">Prof. Stark</td>
                                <td class="px-6 py-4 text-xs font-mono text-indigo-600 font-bold">FRIDAY | 08:00 - 11:00</td>
                                <td class="px-6 py-4 text-center">
                                    <button onclick="addCourseToTimetable('Software Engineering', 'Prof. Stark', 'FRIDAY', '08:00', '11:00')" class="bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold py-2 px-4 rounded-xl transition-all shadow-sm">Select Course</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="w-full max-w-4xl p-8 bg-white/80 backdrop-blur-xl rounded-3xl shadow-2xl border border-white/60">
                <div class="border-b pb-2 mb-4 flex justify-between items-center">
                    <div>
                        <h2 class="text-lg font-bold text-gray-900 tracking-tight">Live Generated Timetable Matrix</h2>
                        <p class="text-xs text-gray-400">Courses successfully passed through conflict verification rules dump here</p>
                    </div>
                    <button onclick="resetTimetableCache()" class="text-xs text-rose-500 font-bold hover:underline">Clear Calendar</button>
                </div>
                
                <div class="overflow-x-auto rounded-2xl border border-gray-200 bg-white/50 shadow-inner">
                    <table class="w-full table-fixed border-collapse text-center text-xs">
                        <thead>
                            <tr class="bg-gray-100 text-gray-700 border-b border-gray-200">
                                <th class="p-3 font-bold border-r w-20 bg-gray-100/80">Time Block</th>
                                <th class="p-3 font-bold border-r">Mon</th>
                                <th class="p-3 font-bold border-r">Tue</th>
                                <th class="p-3 font-bold border-r">Wed</th>
                                <th class="p-3 font-bold border-r">Thu</th>
                                <th class="p-3 font-bold border-r">Fri</th>
                                <th class="p-3 font-bold">Sat</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200">
                            <c:forEach var="hour" begin="8" end="19">
                                <c:set var="hourStr" value="${hour < 10 ? '0' : ''}${hour}" />
                                <tr class="h-14 hover:bg-slate-50/50">
                                    <td class="bg-gray-50/70 font-mono font-medium text-gray-500 border-r p-2">${hourStr}:00</td>
                                    <td id="MONDAY-${hourStr}" class="border-r p-1 transition-all"></td>
                                    <td id="TUESDAY-${hourStr}" class="border-r p-1 transition-all"></td>
                                    <td id="WEDNESDAY-${hourStr}" class="border-r p-1 transition-all"></td>
                                    <td id="THURSDAY-${hourStr}" class="border-r p-1 transition-all"></td>
                                    <td id="FRIDAY-${hourStr}" class="border-r p-1 transition-all"></td>
                                    <td id="SATURDAY-${hourStr}" class="p-1 transition-all"></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>

        <script>
            // Pull existing selections out of browser cache memory stack strings
            let timetableCache = JSON.parse(sessionStorage.getItem('demoTimetable')) || [];

            function addCourseToTimetable(name, teacher, day, startTime, endTime) {
                // Prevent choosing the exact same course twice
                if (timetableCache.some(item => item.name === name)) {
                    alert("⚠️ Notice: This specific course is already active on your timetable!");
                    return;
                }

                const newStart = convertTimeToDecimal(startTime);
                const newEnd = convertTimeToDecimal(endTime);

                // 🌟 LIVE TIME CONFLICT AND OVERLAP ALGORITHM
                for (let course of timetableCache) {
                    if (course.day === day) {
                        const existingStart = convertTimeToDecimal(course.startTime);
                        const existingEnd = convertTimeToDecimal(course.endTime);

                        // Overlap condition logic intersection boundaries
                        if (newStart < existingEnd && newEnd > existingStart) {
                            alert("❌ SCHEDULING CONFLICT DETECTED!\n\nCannot add '" + name + "'. It overlaps with your current choice: '" + course.name + "' (" + course.startTime + " - " + course.endTime + ").");
                            return; 
                        }
                    }
                }

                // If no overlap, commit parameters into sessionStorage
                timetableCache.push({ name, teacher, day, startTime, endTime });
                sessionStorage.setItem('demoTimetable', JSON.stringify(timetableCache));
                
                renderTimetableCells();
            }

            // Converts time characters down to fractional floats (e.g., "08:30" -> 8.5)
            function convertTimeToDecimal(timeStr) {
                const [hours, minutes] = timeStr.split(':').map(Number);
                return hours + (minutes / 60);
            }

            function renderTimetableCells() {
    // 1. Clear out all timetable cells first
    document.querySelectorAll("[id*='-']").forEach(cell => {
        cell.innerHTML = "";
        cell.className = "border-r p-1 transition-all"; // Reset baseline classes
    });

    // 2. Loop through each enrolled course
    timetableCache.forEach(course => {
        const startHour = parseInt(course.startTime.split(':')[0]);
        const endHour = parseInt(course.endTime.split(':')[0]);
        const totalHours = endHour - startHour; // Calculate row span length

        // 🌟 NEW: Loop to cover every hour block the course takes up!
        for (let i = 0; i < totalHours; i++) {
            const currentHour = startHour + i;
            // Pad single digits back to string format (e.g., 9 -> "09")
            const currentHourStr = currentHour < 10 ? '0' + currentHour : currentHour;
            
            const cellId = course.day.toUpperCase() + "-" + currentHourStr;
            const cell = document.getElementById(cellId);
            
            if (cell) {
                // Determine styling borders based on whether it's the top, middle, or bottom of the block
                let roundedClass = "rounded-none";
                let displayText = "";

                if (i === 0) {
                    // Top cell: Show rounded corners at top, and print the Course Name
                    roundedClass = "rounded-t-xl border-t border-x";
                    displayText = `<span class="font-bold block truncate">${course.name}</span>`;
                } else if (i === totalHours - 1) {
                    // Bottom cell: Show rounded corners at bottom, and print the Teacher Name
                    roundedClass = "rounded-b-xl border-b border-x";
                    displayText = `<span class="text-[9px] text-indigo-200 block truncate">${course.teacher}</span>`;
                } else {
                    // Middle cell (for 3+ hour courses): Keep flat borders
                    roundedClass = "border-x";
                }

                // Add background color styles and inject layout text components
                cell.innerHTML = `
                    <div class="bg-indigo-600 text-white px-2 py-1 h-full flex flex-col justify-center items-center text-[10px] leading-tight border-indigo-700 ${roundedClass}">
                        ${displayText}
                    </div>
                `;
                
                // Remove padding on the parent cell wrapper so the blocks join together seamlessly
                cell.className = "border-r p-0 transition-all";
            }
        }
    });
}

            function resetTimetableCache() {
                if (confirm("Are you sure you want to completely wipe the current layout matrix?")) {
                    timetableCache = [];
                    sessionStorage.removeItem('demoTimetable');
                    renderTimetableCells();
                }
            }

            // Execute grid painter system right upon DOM assembly compilation
            window.onload = renderTimetableCells;
        </script>
    </body>
</html>