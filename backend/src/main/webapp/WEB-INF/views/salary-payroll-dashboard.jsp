<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Payroll Management</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-[#f3f4f6] font-sans antialiased text-gray-800">

    <div class="p-8 max-w-7xl mx-auto space-y-8">
        
        <div class="bg-[#111827] rounded-3xl p-6 shadow-2xl text-white">
            <div class="flex justify-between items-center mb-6 px-2">
                <h1 class="text-2xl font-bold tracking-tight">Payroll Management</h1>
                <span class="bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 px-3 py-1 rounded-lg text-sm font-medium">May 2026</span>
            </div>
            
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                <div class="bg-[#1f2937] p-5 rounded-2xl border border-gray-800">
                    <span class="text-xs text-gray-400 uppercase tracking-wider block">Total Payroll</span>
                    <span class="text-3xl font-bold block mt-1">$45,280</span>
                </div>
                <div class="bg-[#1f2937] p-5 rounded-2xl border border-gray-800">
                    <span class="text-xs text-gray-400 uppercase tracking-wider block">Disbursed</span>
                    <span class="text-3xl font-bold block mt-1 text-emerald-400">32</span>
                </div>
                <div class="bg-[#1f2937] p-5 rounded-2xl border border-gray-800">
                    <span class="text-xs text-gray-400 uppercase tracking-wider block">Pending Approval</span>
                    <span class="text-3xl font-bold block mt-1 text-amber-400">5</span>
                </div>
                <div class="bg-[#1f2937] p-5 rounded-2xl border border-gray-800">
                    <span class="text-xs text-gray-400 uppercase tracking-wider block">On Hold</span>
                    <span class="text-3xl font-bold block mt-1 text-rose-400">0</span>
                </div>
            </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            
            <%-- <c:forEach var="payroll" items="${payrollList}"> --%>
            
            <div class="bg-white rounded-2xl p-6 shadow-md border border-gray-100 flex flex-col justify-between">
                <div>
                    <div class="flex justify-between items-center mb-4">
                        <div class="flex items-center gap-3">
                            <div class="w-10 h-10 bg-indigo-100 rounded-full flex items-center justify-center font-bold text-indigo-600">
                                TN </div>
                            <div>
                                <h3 class="font-bold text-gray-900">Thành Nguyễn</h3>
                                <span class="text-xs text-gray-400">ID: #TCH-9922</span>
                            </div>
                        </div>
                        <span class="bg-emerald-50 text-emerald-600 text-xs font-bold px-2.5 py-1 rounded-full border border-emerald-200">Disbursed</span>
                    </div>

                    <div class="space-y-2 text-sm bg-gray-50 p-3 rounded-xl my-4">
                        <div class="flex justify-between">
                            <span class="text-gray-500">Basic Salary:</span>
                            <span class="font-medium text-gray-800">$2,500.00</span>
                        </div>
                        <div class="flex justify-between">
                            <span class="text-gray-500">Class Standing Allowance:</span>
                            <span class="font-medium text-gray-800">$350.00</span>
                        </div>
                        <div class="flex justify-between border-t border-gray-200/60 pt-2 mt-2">
                            <span class="font-semibold text-gray-900">Net Pay:</span>
                            <span class="font-bold text-indigo-600">$2,850.00</span>
                        </div>
                    </div>
                </div>

                <button class="w-full mt-2 bg-white border border-gray-200 hover:bg-gray-50 text-gray-700 text-sm font-semibold py-2.5 rounded-xl transition-colors shadow-sm flex justify-center items-center gap-1">
                    View Pay Stub ↗
                </button>
            </div>
            
            <%-- </c:forEach> --%>

        </div>
    </div>

</body>
</html>