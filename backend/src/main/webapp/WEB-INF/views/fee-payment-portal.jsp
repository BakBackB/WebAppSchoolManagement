<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-store, must-revalidate">
        <title>Fee Payment Portal</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-[#f3f4f6] font-sans antialiased text-gray-800">
      <nav class="bg-white border-b border-gray-200 p-4 flex gap-8 justify-center shadow-sm relative z-20 w-full">
    <a href="<c:url value='/payment'/>" 
       class="text-sm font-semibold text-blue-600 hover:text-blue-800 border-b-2 border-blue-600 pb-1 transition-colors">
       Fee Payment
    </a>
    <a href="<c:url value='/subjects'/>" 
       class="text-sm font-semibold text-gray-600 hover:text-gray-900 pb-1 transition-colors">
       Manage Subjects
    </a>
</nav>
        <div class="flex min-h-screen">
            
            <div class="w-64 bg-white p-6 border-r border-gray-200 flex flex-col gap-6">
                <h2 class="text-xl font-bold text-gray-900">Invoice Filters</h2>
                <div class="flex flex-col gap-3">
                    <label class="flex items-center gap-3 cursor-pointer">
                        <input type="checkbox" checked class="w-4 h-4 rounded text-indigo-600 border-gray-300 focus:ring-indigo-500">
                        <span class="text-sm font-medium">Unpaid</span>
                    </label>
                    <label class="flex items-center gap-3 cursor-pointer">
                        <input type="checkbox" checked class="w-4 h-4 rounded text-indigo-600 border-gray-300 focus:ring-indigo-500">
                        <span class="text-sm font-medium">In Progress</span>
                    </label>
                    <label class="flex items-center gap-3 cursor-pointer">
                        <input type="checkbox" class="w-4 h-4 rounded text-indigo-600 border-gray-300 focus:ring-indigo-500">
                        <span class="text-sm font-medium">Completed</span>
                    </label>
                </div>
            </div>

            <div class="flex-1 p-8">
                <div class="bg-[#1e1b4b] text-white p-6 rounded-2xl shadow-xl mb-8 flex justify-between items-center">
                    <div>
                        <span class="text-xs text-indigo-200 uppercase tracking-wider">School Portal</span>
                        <h1 class="text-2xl font-bold mt-1">Fee Payment Portal</h1>
                    </div>
                    <div class="bg-indigo-900/50 px-4 py-2 rounded-xl border border-indigo-700">
                        <span class="text-xs text-indigo-300 block">Current Total Debt</span>
                        <span class="text-xl font-bold text-emerald-400">$250.00</span>
                    </div>
                </div>
                <c:if test="${not empty param.error}">
                    <div id="flash-message" class="bg-red-50 border border-red-400 text-red-700 px-4 py-3 rounded relative transition-opacity duration-500 ease-in-out opacity-100">${param.error}</div>
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
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    
                    <%-- <c:forEach var="invoice" items="${invoiceList}"> --%>
                    
                    <div class="bg-white rounded-2xl p-6 shadow-lg border border-gray-100 hover:shadow-xl transition-all relative overflow-hidden">
                        <div class="flex justify-between items-start mb-4">
                            <div>
                                <h3 class="font-bold text-gray-900 text-lg">Tuition Fee - Semester I</h3>
                                <span class="text-xs text-gray-400">Invoice code: #INV-202601</span>
                            </div>
                            <span class="bg-amber-50 text-amber-600 text-xs font-bold px-3 py-1 rounded-full border border-amber-200">Unpaid</span>
                        </div>

                        <div class="space-y-3 my-6 border-y border-gray-50 py-4">
                            <div class="flex justify-between text-sm">
                                <span class="text-gray-500">Tuition Fee</span>
                                <span class="font-semibold text-gray-800">$200.00</span>
                            </div>
                            <div class="flex justify-between text-sm">
                                <span class="text-gray-500">Facility Fee</span>
                                <span class="font-semibold text-gray-800">$50.00</span>
                            </div>
                        </div>

                        <div class="flex justify-between items-center pt-2">
                            <div>
                                <span class="text-xs text-gray-400 block">Payment Due Date</span>
                                <span class="text-sm font-medium text-rose-500">30/06/2026</span>
                            </div>
                            <button class="bg-[#1e1b4b] text-white text-sm font-semibold px-5 py-2.5 rounded-xl hover:bg-indigo-900 transition-colors shadow-md flex items-center gap-1">
                                Pay Now <span class="text-xs">↗</span>
                            </button>
                        </div>
                    </div>
                    
                    <%-- </c:forEach> --%>

                </div>
            </div>
        </div>

    </body>
</html>