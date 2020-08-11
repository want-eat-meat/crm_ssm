/**
 *折线图
 * @param datas
 * {
 *     id:要绑定的div
 *     name:显示的名字
 *     key:键名
 *     value:值
 * }
 */
function chart_line(datas){
    var main = echarts.init(document.getElementById(datas.id));
    var colors = ['#5793f3', '#d14a61', '#675bba'];
    var option = {
        color: colors,
        tooltip: {
            trigger: 'none',
            axisPointer: {
                type: 'cross'
            }
        },
        legend: {
            data:[datas.name]
        },
        grid: {
            top: 70,
            bottom: 50
        },
        xAxis: [
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: colors[0]
                    }
                },
                axisPointer: {
                    label: {
                        formatter: function (params) {
                            return datas.name + params.value
                                + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
                        }
                    }
                },
                data: datas.key,
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: datas.name,
                type: 'line',
                smooth: false,
                data: datas.value
            }
        ]
    };
    main.setOption(option);
}

/**
 *柱状图
 * @param datas
 * {
 *     id:要绑定的div
 *     name:显示的名字
 *     key:键名
 *     value:值
 * }
 */
function chart_bar(datas){
    var main = echarts.init(document.getElementById(datas.id));
    var option = {
        color: ['#3398DB'],
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: datas.key,
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: datas.name,
                type: 'bar',
                barWidth: '60%',
                data: datas.value
            }
        ]
    };
    main.setOption(option);
}

/**
 *饼状图
 * @param datas
 * {
 *     id:要绑定的div
 *     name:显示的名字
 *     key:键名
 *     data:JSON数组[{name:"",value :""}]
 * }
 */
function chart_circle(datas){
    var main = echarts.init(document.getElementById(datas.id));
    var option = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
            orient: 'vertical',
            left: 10,
            data: datas.key
        },
        series: [
            {
                name: datas.name,
                type: 'pie',
                radius: ['50%', '70%'],
                avoidLabelOverlap: false,
                label: {
                    show: true,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: '30',
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: datas.data
            }
        ]
    };
    main.setOption(option);
}

/**
 * 漏斗图
 * @param datas
 * {
 *     id:要绑定的div
 *     name:显示的名字
 *     key:键名
 *     size:数据大小
 *     data:JSON数组[{name:"",value :""}]
 * }
 */
function chart_funnel(datas){
    var main = echarts.init(document.getElementById(datas.id));
    var option = {
        title: {
            text: '漏斗图',
            subtext: datas.name
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c}条"
        },
        toolbox: {
            feature: {
                dataView: {readOnly: false},
                restore: {},
                saveAsImage: {}
            }
        },
        legend: {
            data: datas.key
        },

        series: [
            {
                name:datas.name,
                type:'funnel',
                left: '10%',
                top: 60,
                //x2: 80,
                bottom: 60,
                width: '80%',
                // height: {totalHeight} - y - y2,
                min: 0,
                max: datas.size,
                minSize: '0%',
                maxSize: '100%',
                sort: 'descending',
                gap: 2,
                label: {
                    show: true,
                    position: 'inside'
                },
                labelLine: {
                    length: 10,
                    lineStyle: {
                        width: 1,
                        type: 'solid'
                    }
                },
                itemStyle: {
                    borderColor: '#fff',
                    borderWidth: 1
                },
                emphasis: {
                    label: {
                        fontSize: 20
                    }
                },
                data: datas.data
            }
        ]
    };
    main.setOption(option);
}