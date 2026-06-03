<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-store, must-revalidate">
        <title>Fee Payment Portal</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="relative min-h-screen bg-slate-50 font-sans antialiased text-gray-800 overflow-x-hidden">
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
        <%-- Gradient Background Decorative Blobs --%>
        <div class="fixed top-[-10%] left-[-5%] w-96 h-96 bg-indigo-300 rounded-full mix-blend-multiply filter blur-[120px] opacity-50 pointer-events-none"></div>
        <div class="fixed top-[20%] right-[-5%] w-[30rem] h-[30rem] bg-emerald-300 rounded-full mix-blend-multiply filter blur-[120px] opacity-40 pointer-events-none"></div>
        <div class="fixed bottom-[-10%] left-[25%] w-[40rem] h-[40rem] bg-violet-300 rounded-full mix-blend-multiply filter blur-[120px] opacity-40 pointer-events-none"></div>

        <%-- Main Content --%>
        <div class="relative z-10 p-8 max-w-7xl mx-auto space-y-8">

            <%-- ── Header card ─────────────────────────────────────────────── --%>
            <div class="bg-[#111827] rounded-3xl p-6 shadow-2xl text-white mb-4">
                <div class="flex justify-between items-center mb-4 px-2">
                    <h1 class="flex items-center text-4xl font-bold tracking-tight gap-x-2">Welcome,
                        <span class="bg-indigo-500/10 text-indigo-400 border border-indigo-500/20 px-3 py-1 rounded-lg text-xl font-medium">
                            ${sessionScope.role}
                        </span>
                    </h1>
                    <form action="logout" method="post">
                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                        <button type="submit" class="bg-rose-500/10 text-rose-400 border border-rose-500/20 px-3 py-1 rounded-lg text-sm hover:bg-gray-50 transition duration-300 ease-in-out px-4 py-2 shadow-sm">
                            Log out
                        </button>
                    </form>
                </div>

                <div class="flex justify-between items-center mb-6 px-2">
                    <div class="flex flex-nowrap items-center gap-x-3">
                        <h1 class="text-2xl font-bold tracking-tight">Fee Payment Portal</h1>
                        <c:if test="${not empty student}">
                            <span class="bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 px-3 py-1 rounded-lg text-xs font-medium">
                                ${student.studentCode}
                            </span>
                        </c:if>
                    </div>
                </div>

                <%-- ── Summary stats ─────────────────────────────────────── --%>
                <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                    <div class="bg-[#1f2937] p-5 rounded-2xl border border-gray-800">
                        <span class="text-xs text-gray-400 uppercase tracking-wider block">Student</span>
                        <span class="text-xl font-bold block mt-1 truncate
                            drop-shadow-[0_0_10px_rgba(255,255,255,0.8),_0_0_20px_rgba(255,255,255,0.5)]">
                            ${not empty student ? student.studentName : '—'}
                        </span>
                    </div>
                    <div class="bg-[#1f2937] p-5 rounded-2xl border border-gray-800">
                        <span class="text-xs text-gray-400 uppercase tracking-wider block">Total Outstanding Debt</span>
                        <span class="text-3xl font-bold block mt-1 text-rose-400
                            drop-shadow-[0_0_10px_rgba(255,107,107,0.8),_0_0_20px_rgba(255,107,107,0.5)]">
                            $${not empty totalDebt ? totalDebt : '0.00'}
                        </span>
                    </div>
                    <div class="bg-[#1f2937] p-5 rounded-2xl border border-gray-800">
                        <span class="text-xs text-gray-400 uppercase tracking-wider block">Major</span>
                        <span class="text-xl font-bold block mt-1 truncate
                            drop-shadow-[0_0_10px_rgba(255,255,255,0.8),_0_0_20px_rgba(255,255,255,0.5)]">
                            ${not empty student ? student.major : '—'}
                        </span>
                    </div>
                </div>
            </div>

            <%-- ── Flash messages ──────────────────────────────────────────── --%>
            <c:if test="${not empty flashSuccess}">
                <div id="flash-success" class="bg-emerald-50 border border-emerald-400 text-emerald-700 px-4 py-3 rounded relative transition-opacity duration-500 ease-in-out opacity-100" role="alert">
                    <span class="block sm:inline">${flashSuccess}</span>
                </div>
            </c:if>
            <c:if test="${not empty flashError}">
                <div id="flash-error" class="bg-red-50 border border-red-400 text-red-700 px-4 py-3 rounded relative transition-opacity duration-500 ease-in-out opacity-100" role="alert">
                    <span class="block sm:inline">${flashError}</span>
                </div>
            </c:if>
            <%-- Also catch error passed as a URL param (e.g. from filter redirects) --%>
            <c:if test="${not empty param.error}">
                <div id="flash-param-error" class="bg-red-50 border border-red-400 text-red-700 px-4 py-3 rounded relative transition-opacity duration-500 ease-in-out opacity-100" role="alert">
                    <span class="block sm:inline">${param.error}</span>
                </div>
            </c:if>

            <%-- ── Fee cards grid ──────────────────────────────────────────── --%>
            <c:choose>
                <c:when test="${empty fees}">
                    <div class="text-center py-20 text-gray-400">
                        <p class="text-xl font-semibold">No fee records found.</p>
                        <p class="text-sm mt-2">You're all caught up!</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        <c:forEach var="fee" items="${fees}">

                            <%-- Determine badge style by status --%>
                            <c:set var="badgeClass" value="bg-amber-50 text-amber-600 border-amber-200" />
                            <c:if test="${fee.status == 'PAID'}">
                                <c:set var="badgeClass" value="bg-emerald-50 text-emerald-600 border-emerald-200" />
                            </c:if>
                            <c:if test="${fee.status == 'UNPAID'}">
                                <c:set var="badgeClass" value="bg-rose-50 text-rose-600 border-rose-200" />
                            </c:if>

                            <div class="bg-white rounded-2xl p-6 shadow-md border border-gray-100 flex flex-col justify-between hover:shadow-xl transition-all">
                                <div>
                                    <div class="flex justify-between items-start mb-4">
                                        <div>
                                            <h3 class="font-bold text-gray-900 text-lg">Invoice #INV-${fee.feeId}</h3>
                                            <span class="text-xs text-gray-400">Fee ID: ${fee.feeId}</span>
                                        </div>
                                        <span class="${badgeClass} text-xs font-bold px-3 py-1 rounded-full border">
                                            ${fee.status}
                                        </span>
                                    </div>

                                    <div class="space-y-2 text-sm bg-gray-50 p-3 rounded-xl my-4">
                                        <div class="flex justify-between">
                                            <span class="text-gray-500">Amount:</span>
                                            <span class="font-semibold text-gray-800">$${fee.amount}</span>
                                        </div>
                                        <div class="flex justify-between">
                                            <span class="text-gray-500">Due Date:</span>
                                            <span class="font-medium
                                                ${fee.status == 'UNPAID' ? 'text-rose-500' : 'text-gray-800'}">
                                                ${fee.dueDate}
                                            </span>
                                        </div>
                                        <c:if test="${not empty fee.paymentDate}">
                                            <div class="flex justify-between border-t border-gray-200/60 pt-2 mt-2">
                                                <span class="text-gray-500">Paid On:</span>
                                                <span class="font-medium text-emerald-600">${fee.paymentDate}</span>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>

                                <%-- Pay Now button — only shown for unpaid/UNPAID fees --%>
                                <c:choose>
                                    <c:when test="${fee.status == 'PAID'}">
                                        <div class="w-full mt-2 bg-emerald-50 border border-emerald-200 text-emerald-600 text-sm font-semibold py-2.5 rounded-xl flex justify-center items-center gap-1 cursor-default select-none">
                                            ✓ Paid
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <form action="payment" method="post">
                                            <input type="hidden" name="action" value="pay">
                                            <input type="hidden" name="feeId" value="${fee.feeId}">
                                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                            <button type="submit"
                                                class="w-full mt-2 bg-[#1e1b4b] text-white text-sm font-semibold py-2.5 rounded-xl hover:bg-indigo-900 transition-colors shadow-md flex justify-center items-center gap-1">
                                                Pay Now ↗
                                            </button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>

        <%-- Auto-dismiss flash messages after 5 seconds --%>
        <script>
            ['flash-success', 'flash-error', 'flash-param-error'].forEach(id => {
                const el = document.getElementById(id);
                if (!el) return;
                setTimeout(() => {
                    el.classList.remove('opacity-100');
                    el.classList.add('opacity-0');
                    setTimeout(() => el.remove(), 500);
                }, 5000);
            });
        </script>

    </body>
</html>
