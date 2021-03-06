var instMetricId = 0;
var zoomCount = 0;
var favoritesId = 0;
var option;
function loadChart3(hostId1, instMetricId1, title1, favoritesId,username) {
    datetime = 0;
    $.getJSON('/lastDay?hostId=' + hostId1 + '&instMetricId=' + instMetricId1, function (data, status) {
        //console.log($("#"+instMetricId1).attr('favoritesId'));
        $("#" + instMetricId1).attr('favoritesId', favoritesId);
        //console.log($("#"+instMetricId1).attr('favoritesId'));
        onWheel();
        keyEvent();
        buttons();
        chart2(data, title1, instMetricId1,username);
    });

}


function buttons() {
    $('#all').click(function () {
        $.getJSON('/getAll?hostId=' + hostId + '&instMetricId=' + instMetricId, function (data, status) {
            updateChart(data);
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
        console.log('1 min --> 3 min');
        zoomCount = 2;
        $('#' + instMetricId).attr("zoomCount", "2");
        $.getJSON('/chartClickTheeMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + "&date=" + datetime, function (data, status) {
            updateChart(data);
        });

    } else if (zoomCount == 2) {
        console.log('3 min --> 1 hour');
        zoomCount = 1;
        $('#' + instMetricId).attr("zoomCount", "1");
        $.getJSON('/chartClickHour?hostId=' + hostId + '&instMetricId=' + instMetricId + "&date=" + datetime, function (data, status) {
            updateChart(data);
        });
    } else if (zoomCount == 1) {
        zoomCount = 0;
        console.log('1 hour --> day');
        $('#' + instMetricId).attr("zoomCount", "0");
        $.getJSON('/getValuesDay?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            updateChart(data);
        });
    } else if (zoomCount == 0) {
        console.log('1 day --> 1 month');
        zoomCount = -1;
        //console.log($('#'+instMetricId).attr('zoomCount'));
        $('#' + instMetricId).attr("zoomCount", "-1");
        //console.log($('#'+instMetricId).attr('zoomCount'));
        $.getJSON('/getValuesMonth?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            updateChart(data);
        });
    } else if (zoomCount == -1) {
        zoomCount = -2;
        $('#' + instMetricId).attr("zoomCount", "-2");
        console.log('1 month --> 1 Year');
        $.getJSON('/getValuesYear?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            updateChart(data);
        });
    }


}
function plus() {
    if (zoomCount == -2) {
        console.log('1 year --> 1 month');
        zoomCount = -1;
        $('#' + instMetricId).attr("zoomCount", zoomCount);
        $.getJSON('/getValuesMonth?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            updateChart(data);
        });
    } else if (zoomCount == -1) {
        console.log('1 month --> 1 day');
        zoomCount = 0;
        $('#' + instMetricId).attr("zoomCount", zoomCount);
        $.getJSON('/getValuesDay?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            updateChart(data);
        });
    } else if (zoomCount == 0) {
        console.log('day --> 1 hour');
        zoomCount = 1;
        $('#'+instMetricId).attr("zoomCount",zoomCount);
        $.getJSON('/chartClickHour?hostId=' + hostId + '&instMetricId=' + instMetricId + "&date=" + datetime, function (data, status) {
            updateChart(data);
        });
    } else if (zoomCount == 1) {
        console.log('1 hour --> 3 min');
        zoomCount = 2;
        $('#' + instMetricId).attr("zoomCount", zoomCount);
        $.getJSON('/chartClickTheeMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + "&date=" + datetime, function (data, status) {
            updateChart(data);
        });
    } else if (zoomCount == 2) {
        console.log('3 min --> 1 min');
        zoomCount = 3;
        $('#' + instMetricId).attr("zoomCount", zoomCount);
        $.getJSON('/chartClickOneMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + "&date=" + datetime, function (data, status) {
            updateChart(data);
        });
    }
}
function onWheel() {
    //console.log('onWheel');

    $('div.charts').mouseenter(function () {
        //console.log($(this).attr('id'));
        instMetricId = $(this).attr('id');
        zoomCount = ($(this).attr('zoomCount'));
        favoritesId = ($(this).attr('favoritesId'));
        console.log(favoritesId);
        //console.log(zoomCount);
        weelIn();
    });
    $('div.charts').mouseleave(function () {
        zoomCount = 0;
        weelOut();
    });
}
function weelIn() {
    document.onwheel = function (e) {
        e = e || window.event;
        var delta = e.deltaY || e.detail || e.wheelDelta;
        e.preventDefault ? e.preventDefault() : (e.returnValue = false);
        if (delta < 0) {// - /////////
            plus();
        } else {// + ////////
            min();
        }
    };
}
function weelOut() {
    document.onwheel = function (e) {

    };
}
function keyEvent() {
    //console.log('keyEvent');
    document.onkeydown = function (e) {
        e = e || window.event;
        if (e.shiftKey && e.keyCode == 189) {
            //console.log('Shift min');
            min();
        } else if (e.shiftKey && e.keyCode == 187) {
            //console.log('Shift plus');
            plus();
        }
        return true;
    }
}
function clickEvent() {
    plus();
}

function updateChart(jsonData) {
    var chart = $('#' + instMetricId).highcharts();

    var chartData=[];
    $.each(jsonData.values, function (key, value) {
                var point = [];
                point.push(key * +1);
                point.push(value);
                chartData.push(point);
    });
    chart.series[0].setData(chartData);
    //console.log(chartData);

    chart.redraw();
}


function chart2(jsonData, title, chart_id,username) {
    countPoint = jsonData.count;
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    var chart;
    $('#' + chart_id).highcharts('StockChart', {

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
                    text: 'Dell from home page',
                    onclick: function () {
                        //window.location.href = "/dellFromFavorites?favoritesId=" + favoritesId;
                        $.getJSON( "/dellFromFavorites?favoritesId="+favoritesId, function (metrics) {
                        });
                        alert('Метрика ('+title+') удалена с главного экрана');
                    }
                }
            }
        },

        series: [{
            cursor: 'pointer',
            point: {
                events: {
                    click: function () {
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
                $.each(jsonData.values, function (key, value) {
                    data.push({
                        x: key * +1,
                        y: value,
                        marker: {fillColor: 'blue'}
                    });
                });
                return data;
            })()

        }]
    });
}










function chart3(jsonData, title, chart_id) {
    $('#' + chart_id).highcharts({
        chart: {
            type: 'spline',
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


        exporting: {
            buttons: {
                contextButton: {
                    text: 'Dell from home page',
                    onclick: function () {
                        window.location.href = "/dellFromFavorites?favoritesId=" + favoritesId;
                    }

                },
                exportButton: {
                    text: 'Download',
                    menuItems: Highcharts.getOptions().exporting.buttons.contextButton.menuItems.splice(2)
                },
                printButton: {
                    text: 'Print',
                    onclick: function () {
                        this.print();
                    }
                }
            }
        },

        series: [{
            cursor: 'pointer',
            point: {
                events: {
                    click: function () {
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
                $.each(jsonData.values, function (key, value) {
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

