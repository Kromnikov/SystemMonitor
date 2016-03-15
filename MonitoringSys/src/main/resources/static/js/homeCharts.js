function loadChart3(hostId1, instMetricId1, title1) {
    console.log(hostId1+'-'+instMetricId1);
    datetime = 0;
    $.getJSON('/lastDay?hostId=' + hostId1 + '&instMetricId=' + instMetricId1 , function (data, status) {
        console.log(instMetricId);
        dataJson = data;
        chart2(data, title,instMetricId1);
    });

}




function chart2(jsonData, title,chart_id) {
    countPoint = jsonData.count;
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    var chart;
    $('#'+chart_id).highcharts('StockChart', {

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
                    text: 'Dell from favorites',
                    onclick: function () {
                        window.location.href = "/dellFromFavorites?favoritesId="+0;
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

