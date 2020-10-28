# 实验平台文档

实验平台当前支持三个服务的调用，分别是图像检索，图像去模糊的文档矫正

## 服务规范
要接入实验平台的服务需要实现两个接口：
1. `/run` POST接口，传入的图像文件使用`image`表单参数(form-data)，接口返回结果的图像文件
2. `/heartbeat` GET接口，返回固定值"1"，用于实验平台检测服务是否在线

## 服务配置
服务的接口配置在`application.properties`文件中，示例：
```
dewarp.heartbeat=http://59.67.77.90:7777/heartbeat
dewarp.run=http://59.67.77.90:7777/run

deblur.heartbeat=http://59.67.77.90:6666/heartbeat
deblur.run=http://59.67.77.90:6666/run

retrieval.heartbeat=http://59.67.77.90:8888/heartbeat
retrieval.run=http://59.67.77.90:8888/run
```

## 系统流程
对于一次实验，系统的处理流程如下：
1. 前端调用接口`[POST] /api/{expName}/predict`开始一次实验，路径中的`expName`参数表示实验名，三个实验名分别为：检索（retrieval），去模糊（deblur），矫正（dewarp），传入的图片使用`image`表单参数
2. 平台调用对应的实验服务，将实验结果保存并返回图片的唯一key，以检索服务为例，返回的数据格式如下：
```json
{
    "success": true, \\ 实验是否成功
    "cost": 764, \\ 实验耗时（毫秒）
    "msg": "success", \\ 可自定义的消息
    "results": [
        {
            "index": 0, \\ 结果顺序
            "msg": "12.489995956420898", \\ 可自定义的实验结果信息，此处为检索结果的距离
            "imageKey": "a285a6b6efbb420d930649ac42e5dfa9" \\ 图片的key
        },
        {
            "index": 1,
            "msg": "12.806248664855957",
            "imageKey": "f3a278bc80b14b969a9beed0b696de3d"
        },
        {
            "index": 2,
            "msg": "12.961481094360352",
            "imageKey": "e120c29de529410aa137df60be61a846"
        },
        {
            "index": 3,
            "msg": "13.266499519348145",
            "imageKey": "cc233319dd484a2a9fc59f70dd47d0ad"
        },
        {
            "index": 4,
            "msg": "13.416407585144043",
            "imageKey": "942e44fc86854bf69aa16f451d96a60b"
        },
        {
            "index": 5,
            "msg": "13.416407585144043",
            "imageKey": "2d538994819647a4849fd49227727614"
        },
        {
            "index": 6,
            "msg": "13.56466007232666",
            "imageKey": "4a6d6635915d442c8bef565fc228b69e"
        },
        {
            "index": 7,
            "msg": "13.56466007232666",
            "imageKey": "1a2d5f2c40ce4748bf322d767b77df4e"
        },
        {
            "index": 8,
            "msg": "13.856406211853027",
            "imageKey": "04daca5c37c94f5b809057a069e2d999"
        },
        {
            "index": 9,
            "msg": "13.856406211853027",
            "imageKey": "6118c21159fb4a2fb38f51a5bda47cb2"
        }
    ]
}
```
3. 前端使用图像key调用接口`[GET] /api/image/get?imageKey={key}`获取图像
4. 可使用`[GET] /api/{expName}/heartbeat`接口检测服务状态，并在前端展示服务是否在线
