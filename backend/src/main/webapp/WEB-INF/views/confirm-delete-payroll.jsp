<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-store, must-revalidate">
        <title>Payroll Management - Confirm Deletion</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-[#f3f4f6] font-sans antialiased text-gray-800">
        <div class="p-8 max-w-2xl mx-auto space-y-8 mt-10">
            <div class="bg-white rounded-2xl p-8 shadow-md border border-gray-100">
                <div class="bg-[#111827] rounded-3xl p-3 shadow-2xl text-white mb-4">
                    <div class="flex justify-between items-center px-4">
                        <h1 class="text-2xl font-bold tracking-tight"><span class="text-red-500">⚠</span> Confirm Deletion</h1>
                        <span class="bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 px-3 py-1 rounded-lg text-xs font-medium">${currentTime}</span>
                    </div>
                </div>
                
                <%-- Show the student's details so the user knows exactly what they're deleting --%>
                <div class="bg-gray-50 p-5 rounded-xl border border-gray-200/60 mb-6">
                    <table class="w-full text-sm text-left">
                        <tbody>
                            <tr class="border-b border-gray-200/60"><td class="py-2 text-gray-500">Payroll Code</td><td class="py-2 font-medium text-gray-900">${payroll.getPayrollId()}</td></tr>
                            <tr class="border-b border-gray-200/60"><td class="py-2 text-gray-500">Teacher Code</td><td class="py-2 font-medium text-gray-900">${payroll.getTeacher().getTeacherId()}</td></tr>
                            <tr class="border-b border-gray-200/60"><td class="py-2 text-gray-500">Full Name</td>   <td class="py-2 font-medium text-gray-900">${payroll.getTeacher().getTeacherName()}</td></tr>
                            <tr class="border-b border-gray-200/60"><td class="py-2 text-gray-500">Base Salary</td> <td class="py-2 font-medium text-gray-900">$${payroll.getBaseSalary()}</td></tr>
                            <tr class="border-b border-gray-200/60"><td class="py-2 text-gray-500">Allowances</td>  <td class="py-2 font-medium text-gray-900">$${payroll.getAllowances()}</td></tr>
                            <tr class="border-b border-gray-200/60"><td class="py-2 text-gray-500">Deductions</td>  <td class="py-2 font-medium text-gray-900">$${payroll.getDeductions()}</td></tr>
                            <tr><td class="py-2 text-gray-900 font-semibold">Net Amount</td><td class="py-2 font-bold text-indigo-600">$${payroll.getNetAmount()}</td></tr>
                        </tbody>
                    </table>
                </div>

                <div class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg relative mb-6 text-sm" role="alert">
                    This action is <strong>permanent</strong> and cannot be undone.
                    Are you sure you want to delete this payroll?
                </div>

                <%--
                    PRG Pattern — Step 2:
                    The actual delete is a POST request, so a browser refresh won't
                    accidentally re-delete. After deletion the controller redirects to
                    the list page (GET), completing the Post-Redirect-Get cycle.
                --%>
                <form method="post" action="payroll">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="payrollId" value="${payroll.getPayrollId()}"> 
                    <input type="hidden" name="period" value="${period}">
                    <div class="flex items-center gap-4 mt-4">
                        <button type="submit" class="bg-red-100 border border-red-200 hover:bg-red-200 text-red-700 text-sm font-semibold py-2.5 px-6 rounded-xl transition-colors shadow-sm">
                            Yes, Delete
                        </button>
                        <a href="payroll?period=${period}" class="bg-white border border-gray-200 hover:bg-gray-50 text-gray-700 text-sm font-semibold py-2.5 px-6 rounded-xl transition-colors shadow-sm">
                            Cancel
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>