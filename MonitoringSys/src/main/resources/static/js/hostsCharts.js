var zoom = -5;
var datetime = 0;
var endDatetime = 0;
var flag = 0;
var zoomCount = 0;
var dataJson;
var countPoint;
var hostId;
var instMetricId;
var title;
function loadChart3(hostId1, instMetricId1, title1,datetime1,endDatetime1,username) {
    console.log(datetime1+'-'+endDatetime1+'-'+title1);
    datetime = 0;
    hostId=hostId1;
    instMetricId=instMetricId1;
    title=title1;
    $.getJSON('/getValuesDay?hostId=' + hostId + '&instMetricId=' + instMetricId , function (data, status) {
        console.log(instMetricId);
        onWheel();
        keyEvent();
        buttons();
        dataJson = data;
        chart2(data, title,username);
    });
    //Highcharts.getOptions().exporting.buttons.contextButton.menuItems.push({
    //    text: 'Add to favorites',
    //    onclick: function () {
    //
    //    }
    //});
}


function buttons() {
    console.log('buttons');

    $('#all').click(function () {
        $.getJSON('/getAll?hostId=' + hostId + '&instMetricId=' + instMetricId, function (data, status) {
            chart2(data, title,1);
        });
    });
    $('#min').click(function () {
        min();
    });
    $('#plus').click(function () {
        plus();
    });

}
function min() {
    if (zoomCount == 3) {
        zoomCount = 2;
        console.log('1 min --> 3 min');
        $.getJSON('/chartClickTheeMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId  + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });

    } else if (zoomCount == 2) {
        zoomCount = 1;
        console.log('3 min --> 1 hour');
        $.getJSON('/chartClickHour?hostId=' + hostId + '&instMetricId=' + instMetricId  + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == 1) {
        zoomCount = 0;
        console.log('1 hour --> 1 day');
        $.getJSON('/getValuesDay?hostId=' + hostId + '&instMetricId=' + instMetricId  + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == 0) {
        zoomCount = -1;
        console.log('1 day --> 1 month');
        $.getJSON('/getValuesMonth?hostId=' + hostId + '&instMetricId=' + instMetricId  + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == -1) {
        zoomCount = -2;
            console.log('1 month --> 1 Year');
            $.getJSON('/getValuesYear?hostId=' + hostId + '&instMetricId=' + instMetricId  + "&date=" + datetime, function (data, status) {
                chart2(data, title,1);
            });
    }
}
function plus() {
    if (zoomCount == -2) {
        zoomCount = -1;
        console.log('1 year --> 1 month');
        $.getJSON('/getValuesMonth?hostId=' + hostId + '&instMetricId=' + instMetricId  + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == -1) {
        zoomCount = 0;
        console.log('1 month --> 1 day');
        $.getJSON('/getValuesDay?hostId=' + hostId + '&instMetricId=' + instMetricId  + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == 0) {
        console.log('day --> 1 hour');
        zoomCount = 1;
        $.getJSON('/chartClickHour?hostId=' + hostId + '&instMetricId=' + instMetricId  + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == 1) {
        console.log('1 hour --> 3 min');
        zoomCount = 2;
        $.getJSON('/chartClickTheeMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId  + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == 2) {
        console.log('3 min --> 1 min');
        zoomCount = 3;
        $.getJSON('/chartClickOneMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId  + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    }
}
function onWheel() {
    console.log('onWheel');
    $('#chart_1').mouseenter(function () {
        weelIn();
    });
    $('#chart_1').mouseleave(function () {
        weelOut();
    });
}
function weelIn() {
    document.onwheel = function (e) {
        e = e || window.event;
        var delta = e.deltaY || e.detail || e.wheelDelta;
        if (delta < 0) {// - /////////
            plus();
        } else {// + ////////
            min();
        }
        e.preventDefault ? e.preventDefault() : (e.returnValue = false);
    };
}
function weelOut() {
    document.onwheel = function (e) {

    };
}
function keyEvent() {
    console.log('keyEvent');
    document.onkeydown = function (e) {
        e = e || window.event;
        if (e.shiftKey && e.keyCode == 189) {
            min();
        } else if (e.shiftKey && e.keyCode == 187) {
            plus();
        }
        return true;
    }
}
function clickEvent() {
    plus();
}

function chart2(jsonData, title,username) {
    countPoint = jsonData.count;
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    var chart;
    $('#chart_1').highcharts('StockChart', {

        chart: {
            type: 'area',
            dashStyle:'Dot'
            //events: {
            //    keydown: function () {
            //        alert("error");
            //    }
            //}
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
                    //return (this.value > 0 ? ' + ' : '') + this.value + '%';
                    return this.value;
                }
            },
            plotLines: [{
                value: 0,
                width: 2,
                color: 'silver'
            }]
        },

        plotOptions: {
            //series: {
            //    compare: 'percent'
            //},
            area: {
                fillColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [0, Highcharts.getOptions().colors[0]],
                        [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                    ]
                },
                marker: {
                    radius: 2
                },
                lineWidth: 1,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null
            }
        },

        tooltip: {
            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>',
            valueDecimals: 2
        },

        exporting: {
            buttons: {
                contextButton: {
                    text: 'Download',
                    menuItems: Highcharts.getOptions().exporting.buttons.contextButton.menuItems.splice(2)
                },
                printButton: {
                    text: 'Add to home page',
                    onclick: function () {
                        $.getJSON( "/addToFavorites?hostId="+hostId+"&instMetricId="+instMetricId+"&title="+title+"&user="+username, function (metrics) {
                        });
                        alert('Метрика ('+title+') добавлена на главный экран');
                        //window.location.href = "/addToFavorites?hostId="+hostId+"&instMetricId="+instMetricId+"&title="+title+"&user="+username;
                    }
                }
            }
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
                radius: 2
            },
            data: (function () {
                var data = [];
                //pointQuantity = Object.keys(jsonData).length;
                $.each(jsonData.values, function (key, value) {
                    data.push({
                        x: key * +1,
                        y: value,
                        marker: {fillColor: 'blue'}

                        //y:22.05,
                        //marker: {
                        //    symbol: 'url(https://www.highcharts.com/samples/graphics/sun.png)'
                        //}
                    });
                });
                return data;
            })()

        }]
    });
}
