$(document).ready(function() {

});

function loadChart() {
    $.getJSON('value.json', function(data){
        var items = [];

        $.each(data, function(key, val){
            alert(key);
        });

    });



    //$.getJSON('https://www.highcharts.com/samples/data/jsonp.php?filename=usdeur.json&callback=?', function (json) {
    //
    //Highcharts.setOptions({
    //    global: {
    //        useUTC: false
    //    }
    //});
    //
    //var chart;
    //$('#chart_1').highcharts({
    //    chart: {
    //        type: 'spline',
    //        animation: Highcharts.svg, // don't animate in old IE
    //        marginRight: 10,
    //        events: {
    //            load: function() {
    //
    //                // set up the updating of the chart each second
    //                var series = this.series[0];
    //                var series2 = this.series[1];
    //                setInterval(function() {
    //                    var x = (new Date()).getTime(), // current time
    //                        y = Math.random();
    //                    z = Math.random();
    //                    series.addPoint([x, y], false, true);
    //                    series2.addPoint([x, z], true, true);
    //                }, 1000);
    //            }
    //        }
    //    },
    //    title: {
    //        text: 'CPU'
    //    },
    //    xAxis: {
    //        type: 'datetime',
    //        tickPixelInterval: 100
    //    },
    //    yAxis: [{
    //        title: {
    //            text: 'Value1'
    //        },
    //        plotLines: [{
    //            value: 0,
    //            width: 1,
    //            color: '#808080'
    //        }]
    //    },
    //        {
    //            title: {
    //                text: 'Value2'
    //            },
    //            plotLines: [{
    //                value: 0,
    //                width: 1,
    //                color: '#808080'
    //            }]
    //        }],
    //    tooltip: {
    //        formatter: function() {
    //            return '<b>'+ this.series.name +'</b><br/>'+
    //                Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br/>'+
    //                Highcharts.numberFormat(this.y, 2);
    //        }
    //    },
    //    legend: {
    //        enabled: true
    //    },
    //    exporting: {
    //        enabled: true
    //    },
    //    series: [{
    //        name: 'data',
    //        data: (function() {
    //            // generate an array of random data
    //            var data = [],
    //                time = (new Date()).getTime(),
    //                i;
    //
    //            for (i = -19; i <= 0; i++) {
    //                data.push({
    //                    x: time + i * 1000,
    //                    y: Math.random()
    //                });
    //            }
    //            return data;
    //        })()
    //    },
    //        {
    //            name: 'CPU2',
    //            data: (function() {
    //                // generate an array of random data
    //                var data = [],
    //                    time = (new Date()).getTime(),
    //                    i;
    //
    //                for (i = -19; i <= 0; i++) {
    //                    data.push({
    //                        x: time + i * 1000,
    //                        y: Math.random()
    //                    });
    //                }
    //                return data;
    //            })()
    //        }]
    //});
    //});
}