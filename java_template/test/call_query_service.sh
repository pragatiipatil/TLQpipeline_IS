#!/bin/bash

# JSON object to pass to Lambda Function

json={"\"name\"":"\"Susan\u0020Smith\",\"param1\"":1,\"param2\"":2,\"param3\"":3}


echo "Invoking Lambda function using AWS CLI"
time output=`aws lambda invoke --invocation-type RequestResponse --function-name queryRDS --region us-east-2 --payload $json /dev/stdout | head -n 1 | head -c -2 ; echo`

echo ""
echo "JSON RESULT:"
five_query_data=$(echo $output | jq)
echo $five_query_data | jq .value | sed -r 's/[\"]//g' > query1_result.txt
echo $five_query_data | jq .value2 | sed -r 's/[\"]//g' > query2_result.txt
echo $five_query_data | jq .value3 | sed -r 's/[\"]//g' > query3_result.txt
echo $five_query_data | jq .value4 | sed -r 's/[\"]//g' > query4_result.txt
echo $five_query_data | jq .value5 | sed -r 's/[\"]//g' > query5_result.txt
echo ""
