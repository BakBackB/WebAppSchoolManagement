<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-store, must-revalidate">    
        <title>Financial Reports</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body class="bg-[#f3f4f6] font-sans antialiased text-gray-800">

        <div class="p-8 max-w-7xl mx-auto space-y-8">
            
            <div class="flex justify-between items-center">
                <div>
                    <h1 class="text-2xl font-bold text-gray-900">Financial Statistics</h1>
                    <p class="text-sm text-gray-500">Financial report for tuition revenue and operational expenses.</p>
                </div>
                <div class="flex items-center gap-3">
                    <c:if test="${sessionScope.role eq 'ADMIN'}">
                        <a href="payroll" class="bg-white border border-gray-200 px-4 py-2 rounded-xl text-sm font-medium hover:bg-gray-50 shadow-sm">Payroll Management</a>
                    </c:if>
                    <button class="bg-white border border-gray-200 px-4 py-2 rounded-xl text-sm font-medium hover:bg-gray-50 shadow-sm">Export to Excel</button>
                    <button class="bg-[#1e1b4b] text-white px-4 py-2 rounded-xl text-sm font-medium hover:bg-indigo-900 shadow-sm">Print Report</button>
                    <form action="logout" method="post">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            <button type="submit" class="bg-rose-500/10 text-rose-400 border border-rose-500/20 px-3 py-1 rounded-lg text-sm hover:bg-gray-50 transition duration-300 ease-in-out px-4 py-2 shadow-sm">Log out</button>
                    </form>
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
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
                
                <div class="lg:col-span-2 bg-white p-6 rounded-2xl shadow-md border border-gray-100">
                    <div class="flex justify-between items-center mb-6">
                        <h3 class="font-bold text-gray-900 text-lg">Revenue vs Expense Trend 2026</h3>
                        <span class="text-xs text-gray-400">Unit: USD</span>
                    </div>
                    <div class="h-80 w-full">
                        <canvas id="revenueExpenseChart"></canvas>
                    </div>
                </div>

                <div class="bg-white p-6 rounded-2xl shadow-md border border-gray-100 flex flex-col justify-between">
                    <div>
                        <h3 class="font-bold text-gray-900 text-lg mb-6">Tuition Fee Completion Rate</h3>
                        <div class="h-56 w-full flex justify-center items-center">
                            <canvas id="feeStructureChart"></canvas>
                        </div>
                    </div>
                    <div class="grid grid-cols-2 gap-2 text-center text-xs mt-4 pt-4 border-t border-gray-100">
                        <div>
                            <span class="inline-block w-2 h-2 rounded-full bg-indigo-600 mr-1"></span>
                            <span class="text-gray-500">Paid (85%)</span>
                        </div>
                        <div>
                            <span class="inline-block w-2 h-2 rounded-full bg-rose-400 mr-1"></span>
                            <span class="text-gray-500">Unpaid (15%)</span>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <script>
            // 1. Cấu hình Biểu đồ Cột/Đường kết hợp (Thu vs Chi)
            const ctx1 = document.getElementById('revenueExpenseChart').getContext('2d');
            new Chart(ctx1, {
                type: 'bar',
                data: {
                    labels: ['Month 1', 'Month 2', 'Month 3', 'Month 4', 'Month 5'],
                    datasets: [{
                        label: 'Tuition Revenue',
                        data: [12000, 19000, 3000, 5000, 22000], // Biến dynamic từ Servlet thế vào đây
                        backgroundColor: '#4f46e5', // Màu Indigo đậm hợp thiết kế mẫu
                        borderRadius: 8
                    }, {
                        label: 'Salary Expenses',
                        data: [8000, 8000, 8500, 8000, 9000], // Biến dynamic từ Servlet thế vào đây
                        backgroundColor: '#fda4af', // Màu hồng nhạt tinh tế
                        borderRadius: 8
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: { legend: { position: 'bottom' } },
                    scales: { y: { grid: { color: '#f3f4f6' } }, x: { grid: { display: false } } }
                }
            });

            // 2. Cấu hình Biểu đồ Tròn (Tỷ lệ học phí)
            const ctx2 = document.getElementById('feeStructureChart').getContext('2d');
            new Chart(ctx2, {
                type: 'doughnut',
                data: {
                    labels: ['Paid', 'Unpaid'],
                    datasets: [{
                        data: [85, 15], // Biến dynamic từ Servlet thế vào đây
                        backgroundColor: ['#4f46e5', '#fb7185'],
                        borderWidth: 0
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: { legend: { display: false } },
                    cutout: '75%'
                }
            });
        </script>
    </body>
</html>