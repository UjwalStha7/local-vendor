(function () {
  "use strict";

  var revenueCanvas = document.getElementById("revenueChart");
  var weeklyCanvas = document.getElementById("weeklyChart");
  if (!revenueCanvas || !weeklyCanvas || typeof Chart === "undefined") {
    return;
  }

  var gridColor = "rgba(0, 0, 0, 0.06)";
  var tickColor = "#9ca3af";
  var green = "#22c55e";

  new Chart(revenueCanvas, {
    type: "line",
    data: {
      labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun"],
      datasets: [
        {
          label: "Revenue",
          data: [3500, 5200, 6100, 8500, 11200, 13800],
          borderColor: green,
          backgroundColor: "rgba(34, 197, 94, 0.08)",
          borderWidth: 2.5,
          pointBackgroundColor: "#fff",
          pointBorderColor: green,
          pointBorderWidth: 2,
          pointRadius: 5,
          pointHoverRadius: 6,
          fill: true,
          tension: 0.35
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          backgroundColor: "#111827",
          padding: 10,
          cornerRadius: 8
        }
      },
      scales: {
        x: {
          grid: { display: false },
          ticks: { color: tickColor, font: { size: 12 } }
        },
        y: {
          min: 0,
          max: 14000,
          ticks: {
            stepSize: 3500,
            color: tickColor,
            font: { size: 12 },
            callback: function (v) {
              return v.toLocaleString();
            }
          },
          grid: { color: gridColor }
        }
      }
    }
  });

  new Chart(weeklyCanvas, {
    type: "bar",
    data: {
      labels: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
      datasets: [
        {
          label: "Requests",
          data: [42, 55, 38, 65, 48, 72, 58],
          backgroundColor: green,
          borderRadius: 6,
          borderSkipped: false,
          barPercentage: 0.55,
          categoryPercentage: 0.7
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          backgroundColor: "#111827",
          padding: 10,
          cornerRadius: 8
        }
      },
      scales: {
        x: {
          grid: { display: false },
          ticks: { color: tickColor, font: { size: 12 } }
        },
        y: {
          min: 0,
          max: 80,
          ticks: {
            stepSize: 20,
            color: tickColor,
            font: { size: 12 }
          },
          grid: { color: gridColor }
        }
      }
    }
  });
})();
