function hello() {
    alert("hello");
}


function loadChart3(hostId,instMetricId,title) {
		//alert(title+'->>'+hostId+'->>'+instMetricId);
    $.getJSON('/ajaxtest?hostId='+hostId+'&instMetricId='+instMetricId,function(data,status){
        document.onkeydown = function(e) {
            e = e || window.event;
            if (e.shiftKey && e.keyCode == 189) {
                console.log('Shift + (-)');
            }else
            if (e.shiftKey && e.keyCode == 187) {
                console.log('Shift + (+)');
            }
            return true;
        }

        //$('body').keydown(function(eventObject){
        //    //if(eventObject.which==189 || eventObject.which==187)
        //    {
        //        console.log('Клавиша клавиатуры приведена в нажатое состояние. Код вводимого символа - ' + eventObject.which);
        //        chart2(data,eventObject.which);
        //    }
        //});
        chart2(data,title);
    }).success(function() {
        //alert("success");
    })
    //    .error(function() {
    //    alert("error")
    //});

    //$('#button').click(function() {
    //    chart.series[0].addPoint({marker:{fillColor:'#659355'}, y: Math.random() * 100, color:'#659355'}, true, true);
    //});
}



function chart2(jsonData,title) {
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    var chart;
    $('#chart_1').highcharts('StockChart',{

        chart: {
            type: 'spline',
            events: {
                keydown: function() {
                    alert("error");
                }
            }
        },
        title: {
            text: title
        },
        rangeSelector: {
            selected: 1
        },

        yAxis: {
            labels: {
                formatter: function () {
                    return (this.value > 0 ? ' + ' : '') + this.value + '%';
                }
            },
            plotLines: [{
                value: 0,
                width: 2,
                color: 'silver'
            }]
        },

        plotOptions: {
            series: {
                compare: 'percent'
            }
        },

        tooltip: {
            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>',
            valueDecimals: 2
        },


        series:[{
            name: 'data',
            marker : {
                enabled : true,
                radius : 1
            },
    data: (function() {
        var data = [];
        $.each(jsonData, function(key, value) {

            data.push({
                x: key*+1,
                y: value,
                marker:{ fillColor:'black' }
            });
        });
        return data;
    })()

        }]
    });
}







function chart(jsonData) {

    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });

    var chart;
    $('#chart_1').highcharts('StockChart',{

        chart: {
            type: 'spline',
            animation: Highcharts.svg, // don't animate in old IE
            marginRight: 10,
            events: {
                //load: function() {
                //
                //    // set up the updating of the chart each second
                //    var series = this.series[0];
                //    var series2 = this.series[1];
                //    setInterval(function() {
                //        var x = (new Date()).getTime(), // current time
                //            y = Math.random();
                //        z = Math.random();
                //        series.addPoint([x, y], false, true);
                //        series2.addPoint([x, z], true, true);
                //    }, 1000);
                //}
            }
        },
        title: {
            text: 'CPU'
        },
        xAxis: {
            type: 'datetime',
            tickPixelInterval: 100
        },
        yAxis: [{
            title: {
                text: 'Value1'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
            {
                title: {
                    //text: 'Value2'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            }],
        tooltip: {
            formatter: function() {
                return '<b>'+ this.series.name +'</b><br/>'+
                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br/>'+
                    Highcharts.numberFormat(this.y, 2);
            }
        },
        legend: {
            enabled: true
        },
        exporting: {
            enabled: true
        },
        //series: [{
        //    name: 'data',
        //    data: (function() {
        //        // generate an array of random data
        //        var data = [],
        //            time = (new Date()).getTime(),
        //            i;
        //
        //        for (i = -19; i <= 0; i++) {
        //            data.push({
        //                x: time + i * 1000,
        //                y: Math.random()
        //            });
        //        }
        //        return data;
        //    })()
        //},
        //    {
        //        name: 'CPU2',
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
        //    }]
        series:[{
            name: 'data',
                data: (function() {
                    var data = [];//,
                        //time = (1455802176000),
                        //i;

                    //for (i = -19; i <= 0; i++) {
                    //    data.push({
                    //        x: time + i * 1000,
                    //        y: Math.random()
                    //    });
                    //}
                    $.each(jsonData, function(key, value) {
                        //alert(key + ' -> ' + value);
                        data.push({
                            x: key,
                            y: value
                        });
                    });
                    return data;
                })()
            //data: (function() {
            //    var data = [],
            //        time = (new Date()).getTime(),
            //        i;
            //
            //    for (i = 0; i <= Object.keys(jsonData).length; i++) {
            //        data.push({
            //            x: time + i * 1000,
            //            y: Math.random()
            //        });
            //    }
            //    return data;
            //})()

            //data: (function() {
            //    var data = [],
            //        i= 0,
            //        time = (new Date()).getTime();
            //    //$.each(jsonData, function(key, value) {
            //    //    time[i]=key;
            //    //    i=i+1;
            //    //});
            //    //console.log(time);
            //    //i=0;
            //    //time = [1450185472000,1450185536000,1450185547000,1450185558000,1450185570000];
            //        //time = (new Date()).getTime(),
            //
            //    //time=1450185570000-> key=1450185570000
            //    $.each(jsonData, function(key, value) {
            //        console.log(theObject.toString());
            //        time=myDate.toString();
            //        //console.log('i='+i+' ->time='+time + '-> a=' + a);
            //        data.push({
            //            x: time,
            //            y: value
            //        });
            //        i=i+1;
            //    });
            //    return data;
            //})()


            //data: (function() {
            //    var data = [];
            //    $.each(jsonData, function(key, value) {
            //        //alert(key + ' -> ' + value);
            //        data.push({
            //            x: key+1,
            //            y: value
            //        });
            //    });
            //    return data;
            //})()

            //data : [
            //    [Date.UTC(2006, 0, 29, 0, 0, 0), 30.14],
            //    [Date.UTC(2006, 0, 29, 1, 0, 0), 34.76],
            //    [Date.UTC(2006, 0, 29, 2, 0, 0), 34.34],
            //    [Date.UTC(2006, 0, 29, 3, 0, 0), 30.14],
            //    [Date.UTC(2006, 0, 29, 4, 0, 0), 34.76],
            //    [Date.UTC(2006, 0, 29, 5, 0, 0), 34.34],
            //    [Date.UTC(2006, 0, 29, 6, 10, 0), 33.9]
            //]
        }]
    });
}
