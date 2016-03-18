var zoom = -5;
var datetime = 0;
var flag = 0;
var zoomCount = 0;
var dataJson;
var countPoint;
var hostId;
var instMetricId;
var title;
function loadChart3(hostId1, instMetricId1, title1) {
    //alert(hostId1+'-'+instMetricId1+'-'+title1);
    datetime = 0;
    hostId=hostId1;
    instMetricId=instMetricId1;
    title=title1;
    $.getJSON('/lastDay?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom, function (data, status) {
        console.log(instMetricId);
        onWheel();
        keyEvent();
        buttons();
        dataJson = data;
        chart2(data, title);
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
    //if (zoomCount != 0) {
    if (zoomCount == 3) {
        console.log('1 min --> 3 min');
        zoomCount = 2;
        $.getJSON('/chartClickTheeMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });

    } else if (zoomCount == 2) {
        console.log('3 min --> 1 hour');
        zoomCount = 1;
        $.getJSON('/chartClickHour?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == 1) {
        console.log('1 hour --> day');
        zoomCount = 0;
        $.getJSON('/getValuesDay?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == 0) {
        console.log('1 day --> 3 days');
        zoomCount = -1;
        $.getJSON('/getValuesTheeDays?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == -1) {
        zoomCount = -2;
        console.log('3 days --> 1 month');
        $.getJSON('/getValuesMonth?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == -2) {
        zoomCount = -3;
        console.log('1 month --> 6 months');
        $.getJSON('/getValuesSixMonth?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == -3) {
        zoomCount = -4;
        console.log('6 months --> 1 Year');
        $.getJSON('/getValuesYear?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == -4) {
        zoomCount = -5;
        console.log('1 Year --> All');
        $.getJSON('/getAll?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    }
    //}else {
    //    console.log('zoom = '+(countPoint)+' --> zoom = '+(countPoint+20));
    //    $.getJSON('/getValuesByZoom?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint+20)+ "&date=" + datetime, function (data, status) {
    //        chart2(data, title,1);
    //    });
    //}

}
function plus() {
    if (zoomCount == -5) {
        zoomCount = -4;
        console.log('All --> 1 Year');
        $.getJSON('/getValuesYear?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == -4) {
        zoomCount = -3;
        console.log('1 Year --> 6 months');
        $.getJSON('/getValuesSixMonth?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == -3) {
        zoomCount = -2;
        console.log('6 months --> 1 month');
        $.getJSON('/getValuesMonth?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == -2) {
        console.log('1 month --> 3 days');
        zoomCount = -1;
        $.getJSON('/getValuesTheeDays?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == -1) {
        console.log('3 days --> day');
        zoomCount = 0;
        $.getJSON('/getValuesDay?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + (countPoint + 20) + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == 0) {
        console.log('day --> 1 hour');
        zoomCount = 1;
        $.getJSON('/chartClickHour?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == 1) {
        console.log('1 hour --> 3 min');
        zoomCount = 2;
        $.getJSON('/chartClickTheeMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
            chart2(data, title,1);
        });
    } else if (zoomCount == 2) {
        console.log('3 min --> 1 min');
        zoomCount = 3;
        $.getJSON('/chartClickOneMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
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



    //document.onwheel = function (e) {
    //
    //
    //
    //    //var info = document.getElementById('delta');
    //    $('#chart_1').mouseover(function(){
    //        e = e || window.event;
    //        var delta = e.deltaY || e.detail || e.wheelDelta;
    //        console.log(delta);
    //        //if (delta < 0) {// - /////////
    //        //    min();
    //        //} else {// + ////////
    //        //    plus();
    //        //}
    //        e.preventDefault ? e.preventDefault() : (e.returnValue = false);
    //    });
    //    //if (e.shiftKey) {
    //    //    if (delta < 0) {// - /////////
    //    //        min();
    //    //    } else {// + ////////
    //    //        plus();
    //    //    }
    //    //}
    //    //info.innerHTML = +info.innerHTML + delta;
    //    //e.preventDefault ? e.preventDefault() : (e.returnValue = false);
    //};
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
    //if (zoomCount == 1) {
    //    console.log('1 hour --> 3 min');
    //    zoomCount = 2;
    //    $.getJSON('/chartClickTheeMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
    //        chart2(data, title,1);
    //    });
    //} else if (zoomCount == 2) {
    //    console.log('3 min --> 1 min');
    //    $.getJSON('/chartClickOneMinutes?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
    //        chart2(data, title,1);
    //    });
    //} else {
    //console.log('zoom = ' + (countPoint) + ' --> 1 hour');
    //zoomCount = 1;
    //$.getJSON('/chartClickHour?hostId=' + hostId + '&instMetricId=' + instMetricId + '&zoom=' + zoom + "&date=" + datetime, function (data, status) {
    //    chart2(data, title,1);
    //});
    //}
}

function chart2(jsonData, title) {
    countPoint = jsonData.count;
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    var chart;
    $('#chart_1').highcharts('StockChart', {

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
                    //text: 'Dell from home page',
                    //onclick: function () {
                    //    window.location.href = "/dellFromFavorites?favoritesId=" + favoritesId;
                    //}

                    text: 'Download',
                    menuItems: Highcharts.getOptions().exporting.buttons.contextButton.menuItems.splice(2)
                },
                //exportButton: {
                //    text: 'Download',
                //    menuItems: Highcharts.getOptions().exporting.buttons.contextButton.menuItems.splice(2)
                //},
                printButton: {
                    //text: 'Print',
                    //onclick: function () {
                    //    this.print();
                    //}

                    text: 'Add to home page',
                    onclick: function () {
                        window.location.href = "/addToFavorites?hostId="+hostId+"&instMetricId="+instMetricId+"&title="+title;
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
                radius: 1
            },
            data: (function () {
                var data = [];
                //pointQuantity = Object.keys(jsonData).length;
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