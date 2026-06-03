<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-store, must-revalidate">
        <title>Payroll Management</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="relative min-h-screen bg-slate-50 font-sans antialiased text-gray-800 overflow-x-hidden">
<nav class="bg-white border-b border-gray-200 p-4 flex gap-8 justify-center shadow-sm relative z-20 w-full">
    <a href="${pageContext.request.contextPath}/payroll" 
       class="text-sm font-semibold text-blue-600 hover:text-blue-800 border-b-2 border-blue-600 pb-1 transition-colors">
       Manage Payroll
    </a>
    <a href="${pageContext.request.contextPath}/admin/schedule-dashboard" 
       class="text-sm font-semibold text-gray-600 hover:text-gray-900 pb-1 transition-colors">
       Manage Schedules
    </a>
</nav>
        <%-- Gradient Background Decorative Blobs --%>
        <div class="fixed top-[-10%] left-[-5%] w-96 h-96 bg-purple-300 rounded-full mix-blend-multiply filter blur-[120px] opacity-50 pointer-events-none"></div>
        <div class="fixed top-[20%] right-[-5%] w-[30rem] h-[30rem] bg-cyan-300 rounded-full mix-blend-multiply filter blur-[120px] opacity-40 pointer-events-none"></div>
        <div class="fixed bottom-[-10%] left-[25%] w-[40rem] h-[40rem] bg-indigo-300 rounded-full mix-blend-multiply filter blur-[120px] opacity-40 pointer-events-none"></div>

        <%-- Main Content--%>
        <div class="relative z-10 p-8 max-w-7xl mx-auto space-y-8">
            <div class="bg-[#111827] rounded-3xl p-6 shadow-2xl text-white mb-4">
                <div class="flex justify-between items-center mb-4 px-2">
                <h1 class="flex items-center text-4xl font-bold tracking-tight gap-x-2">Welcome, 
                    <span class="bg-indigo-500/10 text-indigo-400 border border-indigo-500/20 px-3 py-1 rounded-lg text-xl font-medium">${sessionScope.role}</span>
                </h1>
                <form action="logout" method="post">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            <button type="submit" class="bg-rose-500/10 text-rose-400 border border-rose-500/20 px-3 py-1 rounded-lg text-sm hover:bg-gray-50 transition duration-300 ease-in-out px-4 py-2 shadow-sm">Log out</button>
                </form>
                </div>
                <div class="flex justify-between items-center mb-6 px-2">
                    <div class="flex flex-nowrap items-center gap-x-3">
                        <h1 class="text-2xl font-bold tracking-tight">Finanncial Statistics</h1>
                        <span class="bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 px-3 py-1 rounded-lg text-xs font-medium">${currentTime}</span>
                    </div>
                    <a href="financial-statistics" class="bg-indigo-500/10 text-indigo-400 border border-indigo-500/20 px-3 py-1 rounded-lg text-sm hover:bg-gray-50 transition duration-300 ease-in-out px-4 py-2 shadow-sm">Payroll Management</a>                               
                </div>
                
                <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                    <div class="bg-[#1f2937] p-5 rounded-2xl border border-gray-800">
                        <span class="text-xs text-gray-400 uppercase tracking-wider block">Total Payroll</span>
                        <span class="text-3xl font-bold block mt-1 drop-shadow-[0_0_10px_rgba(255,255,255,0.8),_0_0_20px_rgba(255,255,255,0.5),_0_0_40px_rgba(255,255,255,0.2)]">$${totalPayroll}</span>
                    </div>
                    <div class="bg-[#1f2937] p-5 rounded-2xl border border-gray-800">
                        <span class="text-xs text-gray-400 uppercase tracking-wider block">Disbursed</span>
                        <span class="text-3xl font-bold block mt-1 text-emerald-400 drop-shadow-[0_0_10px_rgba(74,222,128,0.8),_0_0_20px_rgba(74,222,128,0.5),_0_0_40px_rgba(74,222,128,0.2)]">${disbursedCount}</span>
                    </div>
                    <div class="bg-[#1f2937] p-5 rounded-2xl border border-gray-800">
                        <span class="text-xs text-gray-400 uppercase tracking-wider block">Pending Approval</span>
                        <span class="text-3xl font-bold block mt-1 text-amber-400 drop-shadow-[0_0_10px_rgba(255,193,7,0.8),_0_0_20px_rgba(255,193,7,0.5),_0_0_40px_rgba(255,193,7,0.2)]">${pendingCount}</span>
                    </div>
                    <div class="bg-[#1f2937] p-5 rounded-2xl border border-gray-800">
                        <span class="text-xs text-gray-400 uppercase tracking-wider block">On Hold</span>
                        <span class="text-3xl font-bold block mt-1 text-rose-400 drop-shadow-[0_0_10px_rgba(255,107,107,0.8),_0_0_20px_rgba(255,107,107,0.5),_0_0_40px_rgba(255,107,107,0.2)]">${onHoldCount}</span>
                    </div>
                </div>
            </div>
            <c:if test="${not empty flashError}">
                <div id="flash-message" class="bg-red-50 border border-red-400 text-red-700 px-4 py-3 rounded relative transition-opacity duration-500 ease-in-out opacity-100" role="alert">
                    <span class="block sm:inline">${flashError}</span>
                </div>
                <script>
                    // Wait 5 seconds (5000 milliseconds), then fade out
                    setTimeout(() => {
                        const flashMsg = document.getElementById('flash-message');
                        if (flashMsg) {
                            // Trigger the Tailwind opacity transition
                            flashMsg.classList.remove('opacity-100');
                            flashMsg.classList.add('opacity-0');
                            
                            // Wait for the 2000ms transition to finish, then remove it from the DOM
                            setTimeout(() => flashMsg.remove(), 500); 
                        }
                    }, 5000);
                    </script>     
            </c:if>
            <div class="flex justify-between items-start mb-6 px-2">
            <%-- Period navigation --%>
                <form method="get" action="payroll" class="px-4 py-2 text-gray-700 bg-white border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-800 dark:border-gray-600 dark:text-gray-200 [&::-webkit-calendar-picker-indicator]:dark:invert">
                    <input type="month" name="period" 
                    value="${selectedPeriod}"
                    onchange="this.form.submit()"
                    />
                </form>

                <%-- Only show Generate if viewing current period and not yet generated --%>
                <c:if test="${selectedPeriod == currentPeriod}">
                    <a href="payroll?action=generate" class="bg-white bg-opacity-75 text-indigo-600 border border-indigo-500/20 px-3 py-1 rounded-lg text-sm font-medium hover:bg-indigo-500 transition duration-300 ease-in-out px-4 py-2 shadow-sm hover:text-white">
                        Generate Payroll for ${currentPeriod}
                    </a>
                            
                </c:if>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                
                <c:forEach var="payroll" items="${payrolls}">
                
                <div class="bg-white rounded-2xl p-6 shadow-md border border-gray-100 flex flex-col justify-between">
                    <div>
                        <div class="flex justify-between items-center mb-4">
                            <div class="flex items-center gap-3">
                                <div class="w-10 h-10 bg-indigo-100 rounded-full flex items-center justify-center font-bold text-indigo-600">
                                    TN </div>
                                <div>
                                    <h3 class="font-bold text-gray-900">${payroll.getTeacher().getTeacherName()}</h3>
                                    <p class="text-xs text-gray-400">Teacher ID: #${payroll.getTeacher().getTeacherId()}</p>
                                    <p class="text-xs text-gray-400">Payroll ID: #${payroll.getPayrollId()}</p>
                                </div>
                            </div>
                            <span class="${payroll.getStatus() == 'DISBURSED' ? 'bg-emerald-50 text-emerald-600 border-emerald-200' : payroll.getStatus() == 'PENDING' ? 'bg-amber-50 text-amber-600 border-amber-200' : 'bg-rose-50 text-rose-600 border-rose-200'} text-xs font-bold px-2.5 py-1 rounded-full border">
                                ${payroll.getStatus()}
                            </span>
                        </div>

                        <div class="space-y-2 text-sm bg-gray-50 p-3 rounded-xl my-4">
                            <div class="flex justify-between">
                                <span class="text-gray-500">Basic Salary:</span>
                                <span class="font-medium text-gray-800">$${payroll.getBaseSalary()}</span>
                            </div>
                            <div class="flex justify-between">
                                <span class="text-gray-500">Class Standing Allowance:</span>
                                <span class="font-medium text-gray-800">$${payroll.getAllowances()}</span>
                            </div>
                            <div class="flex justify-between border-t border-gray-200/60 pt-2 mt-2">
                                <span class="font-semibold text-gray-900">Net Pay:</span>
                                <span class="font-bold text-indigo-600">$${payroll.getNetAmount()}</span>
                            </div>
                        </div>
                    </div>

                    <button class="w-full mt-2 bg-white border border-gray-200 hover:bg-gray-50 text-gray-700 text-sm font-semibold py-2.5 rounded-xl transition-colors shadow-sm flex justify-center items-center gap-1">
                        View Pay Stub ↗
                    </button>
                    <div class="flex justify-between items-center mb-4 gap-x-4">
                    <a href="payroll?action=edit&payrollId=${payroll.getPayrollId()}" class="w-full mt-2 bg-blue-100 border border-blue-200 hover:bg-blue-200 text-blue-700 text-sm font-semibold py-2.5 rounded-xl transition-colors shadow-sm flex justify-center items-center gap-1">
                        Edit
                    </a>
                    <a href="payroll?action=confirmDelete&payrollId=${payroll.getPayrollId()}" class="w-full mt-2 bg-red-100 border border-red-200 hover:bg-red-200 text-red-700 text-sm font-semibold py-2.5 rounded-xl transition-colors shadow-sm flex justify-center items-center gap-1">
                        Delete Payroll
                    </a>
                    </div>
                </div>

                </c:forEach>
            <script>
                const getStatusClasses = (status) => {
                switch (status?.toLowerCase()) {
                    case 'disbursed':
                        return 'bg-emerald-50 text-emerald-600 border-emerald-200';
                    case 'pending':
                        return 'bg-amber-50 text-amber-600 border-amber-200';
                    case 'on_hold':
                        return 'bg-rose-50 text-rose-600 border-rose-200';
                    }
                };
            </script>
            </div>
        </div>
    </body>
</html>