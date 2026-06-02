<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="theme" value="${not empty cookie.user_theme.value ? cookie.user_theme.value : 'light'}" />
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-store, must-revalidate">
        <title>Edit Payroll</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-[#f3f4f6] font-sans antialiased text-gray-800 ${theme}">
        <div class="p-8 max-w-2xl mx-auto space-y-8 mt-10">
            <div class="bg-white rounded-2xl p-8 shadow-md border border-gray-100">
                <div class="bg-[#111827] rounded-3xl p-3 shadow-2xl text-white">
                    <div class="flex justify-between items-center px-4">
                        <h1 class="text-2xl font-bold tracking-tight">Edit Payroll</h1>
                        <span class="bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 px-3 py-1 rounded-lg text-xs font-medium">${currentTime}</span>
                    </div>
                </div>
                
                <form method="post" action="payroll" class="space-y-5">
                    <input type="hidden" name="action" value="update">
                    <c:if test="${payroll != null}">
                        <input type="hidden" name="payrollId" value="${payroll.getPayrollId()}">
                    </c:if>

                    <%-- Payroll Id --%>
                    <div>
                        <label for="payrollId" class="block mb-2 text-sm font-medium text-gray-900">Payroll Id:</label>
                        <input type="text" id="payrollId" name="payrollId"
                               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-500 focus:border-indigo-500 block w-full p-2.5 disabled:bg-gray-200 disabled:text-gray-500 ${not empty errorPayrollId ? 'border-red-500' : ''}"
                               value="${payroll.getPayrollId()}"
                               disabled>
                        <c:if test="${not empty errorPayrollId}">
                            <span class="mt-1 text-sm text-red-600 block">${errorPayrollId}</span>
                        </c:if>
                    </div>

                    <%-- Teacher Id --%>
                    <div>
                        <label for="teacherId" class="block mb-2 text-sm font-medium text-gray-900">Teacher Code:</label>
                        <input type="text" id="teacherId" name="teacherId"
                               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-500 focus:border-indigo-500 block w-full p-2.5 disabled:bg-gray-200 disabled:text-gray-500 ${not empty errorteacherId ? 'border-red-500' : ''}"
                               value="${payroll.getTeacher().getTeacherId()}"
                               disabled>
                        <c:if test="${not empty errorteacherId}">
                            <span class="mt-1 text-sm text-red-600 block">${errorteacherId}</span>
                        </c:if>
                    </div>

                    <%-- Base salary --%>
                    <div>
                        <label for="baseSalary" class="block mb-2 text-sm font-medium text-gray-900">Base Salary:</label>
                        <input type="number" step="0.01" id="baseSalary" name="baseSalary"
                               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-500 focus:border-indigo-500 block w-full p-2.5 ${not empty errorBaseSalary ? 'border-red-500 bg-red-50' : ''}"
                               value="${payroll.getBaseSalary()}"
                               required>
                        <c:if test="${not empty errorBaseSalary}">
                            <span class="mt-1 text-sm text-red-600 block">${errorBaseSalary}</span>
                        </c:if>
                    </div>

                    <%-- Allowances --%>
                    <div>
                        <label for="allowances" class="block mb-2 text-sm font-medium text-gray-900">Allowances:</label>
                        <input type="number" step="0.01" id="allowances" name="allowances"
                               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-500 focus:border-indigo-500 block w-full p-2.5 ${not empty errorAllowances ? 'border-red-500 bg-red-50' : ''}"
                               value="${payroll.getAllowances()}"
                               required>
                        <c:if test="${not empty errorAllowances}">
                            <span class="mt-1 text-sm text-red-600 block">${errorAllowances}</span>
                        </c:if>
                    </div>

                    <%-- Deductions --%>
                    <div>
                        <label for="deductions" class="block mb-2 text-sm font-medium text-gray-900">Deductions:</label>
                        <input type="number" step="0.01" id="deductions" name="deductions"
                               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-500 focus:border-indigo-500 block w-full p-2.5 ${not empty errorDeductions ? 'border-red-500 bg-red-50' : ''}"
                               value="${payroll.getDeductions()}"
                               required>
                        <c:if test="${not empty errorDeductions}">
                            <span class="mt-1 text-sm text-red-600 block">${errorDeductions}</span>
                        </c:if>
                    </div>

                    <%-- Status --%>
                    <div>
                        <label for="status" class="block mb-2 text-sm font-medium text-gray-900">Status:</label>
                        <select id="status" name="status" 
                                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-500 focus:border-indigo-500 block w-full p-2.5 ${not empty errorStatus ? 'border-red-500 bg-red-50' : ''}" required>
                            <option value="">Select Status</option>
                            <option value="PENDING" ${payroll.getStatus() == 'PENDING' ? 'selected' : ''}>PENDING</option>
                            <option value="DISBURSED" ${payroll.getStatus() == 'DISBURSED' ? 'selected' : ''}>DISBURSED</option>
                            <option value="ON_HOLD" ${payroll.getStatus() == 'ON_HOLD' ? 'selected' : ''}>ON_HOLD</option>
                        </select>
                        <c:if test="${not empty errorStatus}">
                            <span class="mt-1 text-sm text-red-600 block">${errorStatus}</span>
                        </c:if>
                    </div>

                    <div class="flex items-center gap-4 pt-4 mt-6 border-t border-gray-200">
                        <button type="submit" class="bg-blue-100 border border-blue-200 hover:bg-blue-200 text-blue-700 text-sm font-semibold py-2.5 px-8 rounded-xl transition-colors shadow-sm">
                            Save
                        </button>
                        <a href="payroll?period=${period}" class="bg-white border border-gray-200 hover:bg-gray-50 text-gray-700 text-sm font-semibold py-2.5 px-8 rounded-xl transition-colors shadow-sm">
                            Cancel
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>