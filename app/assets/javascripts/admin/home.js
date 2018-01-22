d3.json("/admin/countByDay",(err,data) => {
    if (err) {
        return console.warn(error);
    }

    var margin = {top: 20, right: 30, bottom: 30, left: 40},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

    var width = 1000,
        height = 500;


    var x = d3.scaleBand()
	       .rangeRound([0, width])
               .paddingInner(.1);

    var y = d3.scaleLinear()
        .range([height, 0]);

    var xAxis = d3.axisBottom()
        .scale(x)

    var yAxis = d3.axisLeft()
        .scale(y)

    var chart = d3.select(".chart")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    x.domain(data.map(function(d) { return d.date; }));
    y.domain([0, d3.max(data, function(d) { return d.count; })]);

     chart.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  chart.append("g")
      .attr("class", "y axis")
      .call(yAxis)
      .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("Frequency");

  chart.selectAll(".bar")
      .data(data)
    .enter().append("rect")
      .attr("class", "bar")
      .attr("x", function(d) { return x(d.date); })
      .attr("y", function(d) { return y(d.count); })
      .attr("height", function(d) { return height - y(d.count); })
      .attr("width", x.bandwidth());

   d3.select("input").on("change", change);

   var sortTimeout = setTimeout(function() {
    d3.select("input").property("checked", true).each(change);
  }, 2000);

  function change() {
    clearTimeout(sortTimeout);

    // Copy-on-write since tweens are evaluated after a delay.
    var x0 = x.domain(data.sort(this.checked
        ? function(a, b) { return a.count - b.count; }
        : function(a, b) { return d3.ascending(a.date, b.date); })
        .map(function(d) { return d.date; }))
        .copy();

    chart.selectAll(".bar")
        .sort(function(a, b) { return x0(a.date) - x0(b.date); });

    var transition = chart.transition().duration(750),
        delay = function(d, i) { return i * 50; };

    transition.selectAll(".bar")
        .delay(delay)
        .attr("x", function(d) { return x0(d.date); });

    transition.select(".x.axis")
        .call(xAxis)
      .selectAll("g")
        .delay(delay);
  }
})

function type(d) {
    d.value = +d.value; // coerce to number
    return d;
}

fetch("/admin/bestBuy").then(x=>x.json()).then(console.log)