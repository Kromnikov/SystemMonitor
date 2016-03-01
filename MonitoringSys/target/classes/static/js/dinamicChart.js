var zoom = -5;
var datetime = 0;
var flag = 0;
var zoomCount = 0;
function loadChart3(hostId, instMetricId, title) {
    datetime = 0;
    $.getJSON('/ajaxtest?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom, function (data, status) {
        onWheel();
        keyEvent();
        chart2(data, title);
    })

    //$('#button').click(function() {
    //    chart.series[0].addPoint({marker:{fillColor:'#659355'}, y: Math.random() * 100, color:'#659355'}, true, true);
    //});
}

function onWheel() {
    console.log('onWheel');
    document.onwheel = function (e) {

        e = e || window.event;

        var delta = e.deltaY || e.detail || e.wheelDelta;


        //var info = document.getElementById('delta');
        if (e.shiftKey) {
            if (delta < 0) {
                zoom = zoom - 1;
                console.log("delta= " + delta + "  zoom=" + zoom);

            } else {
                //if (zoom <= -5) {
                //    zoom = zoom + 1;
                //    console.log("delta= " + delta + "  zoom=" + zoom);
                if (zoom == -5) {
                    console.log("zoom=" + zoom);
                    zoom = zoom + 1;
                    zoomCount = 1;
                    flag = 1;
                    $.getJSON('/chartClickHour?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
                        chart2(data, title);
                    });
                } else if (zoom == -4) {
                    console.log("zoom=" + zoom);
                    zoom = zoom + 1;
                    zoomCount = 2;
                    $.getJSON('/chartClickMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
                        chart2(data, title);
                    });
                } else if (zoom == -3) {
                    console.log("zoom=" + zoom);
                    zoomCount = 3;
                    $.getJSON('/chartClickSec?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
                        chart2(data, title);
                    });
                }

                else {

                }
            }
        }
        //info.innerHTML = +info.innerHTML + delta;

        //e.preventDefault ? e.preventDefault() : (e.returnValue = false);
    };
}
function keyEvent() {
    console.log('keyEvent');
    document.onkeydown = function (e) {
        e = e || window.event;
        if (e.shiftKey && e.keyCode == 189) {
            console.log('Shift + (min)');
            if (flag > 0) {
                if (zoomCount == 3) {
                    zoomCount = 2;
                    console.log('zoomCount==3 --> 2');
                    $.getJSON('/chartClickMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
                        chart2(data, title);
                    });
                } else if (zoomCount == 2) {
                    console.log('zoomCount==2 --> 1');
                    zoomCount = 1;
                    $.getJSON('/chartClickHour?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
                        chart2(data, title);
                    });

                } else {
                    console.log('zoomCount==1 --> 0');
                    zoomCount = 0;
                    flag = 0;

                }
            }
            else {
                zoom = zoom - 5;
                if (datetime == 0) {
                    $.getJSON('/ajaxtest?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom, function (data, status) {
                        console.log('Без перехода на точку = ' + datetime);
                        chart2(data, title);
                    });
                } else {
                    $.getJSON('/ajaxtest?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
                        console.log('Переход на точку = ' + datetime);
                        chart2(data, title);
                    });
                }
            }
        } else if (e.shiftKey && e.keyCode == 187) {
            console.log('Shift + (plus)');
            if (flag > 0) {
                if (zoomCount == 1) {
                    zoomCount = 2;
                    console.log('zoomCount==1 --> 2');
                    $.getJSON('/chartClickMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
                        chart2(data, title);
                    });
                } else if (zoomCount == 2) {
                    console.log('zoomCount==2 --> 3');
                    zoomCount = 3;
                    $.getJSON('/chartClickSec?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
                        chart2(data, title);
                    });

                }
            }
            else {
                zoom = zoom + 5;
                if (datetime == 0) {
                    $.getJSON('/ajaxtest?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom, function (data, status) {
                        console.log('Без перехода на точку = ' + datetime);
                        chart2(data, title);
                    });
                } else {
                    $.getJSON('/ajaxtest?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
                        console.log('Переход на точку = ' + datetime);
                        chart2(data, title);
                    });
                }
            }
        }
        return true;
    }
}
function clickEvent() {
    if (flag == 1) {
        if (zoomCount < 2) {
            console.log('-1 +1 min');
            zoomCount = 2;
            $.getJSON('/chartClickMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
                chart2(data, title);
            });
        } else {
            console.log('1 min');
            zoomCount = 3;
            $.getJSON('/chartClickSec?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
                chart2(data, title);
            });
        }
    } else {
        console.log('ajaxtest flag=0');
        zoomCount = 1;
        flag = 1;
        $.getJSON('/chartClickHour?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
            chart2(data, title);
        });
    }
}

function chart2(jsonData, title) {
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    var chart;
    $('#chart_1').highcharts('StockChart', {

        chart: {
            type: 'line',
            events: {
                keydown: function () {
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


        series: [{
            cursor: 'pointer',
            point: {
                events: {
                    click: function () {
                        //alert('Category: ' + this.category + ', value: ' + this.y);
                        datetime = this.category;
                        clickEvent();
                    }
                }
            },
            name: 'data',
            marker: {
                enabled: true,
                radius: 1
            },
            data: (function () {
                var data = [];
                $.each(jsonData, function (key, value) {

                    data.push({
                        x: key * +1,
                        y: value,
                        marker: {fillColor: 'black'}
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
    $('#chart_1').highcharts('StockChart', {

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
            formatter: function () {
                return '<b>' + this.series.name + '</b><br/>' +
                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
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
        series: [{
            name: 'data',
            data: (function () {
                var data = [];//,
                //time = (1455802176000),
                //i;

                //for (i = -19; i <= 0; i++) {
                //    data.push({
                //        x: time + i * 1000,
                //        y: Math.random()
                //    });
                //}
                $.each(jsonData, function (key, value) {
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
