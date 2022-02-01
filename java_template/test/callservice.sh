#!/bin/bash

# JSON object to pass to Lambda Function

# json={"\"bucketname\"":\"t562-project\"","\"filename\"":\"1000\u0020Sales\u0020Records.csv\"","\"filename2\"":\"output.csv\""}
json={"\"bucketname\"":"\"t562-project\",\"filename\"":"\"1000\u0020Sales\u0020Records.csv\",\"filename1\"":"\"result.csv\",\"header1\"":"\"Order\u0020Processing\u0020Time\",\"header2\"":"\"Gross\u0020Margin\""}

echo "Invoking Lambda function using AWS CLI"

time output=`aws lambda invoke --invocation-type RequestResponse --function-name processCSV  --region us-east-2 --payload $json /dev/stdout | head -n 1 | head -c -2 ; echo`

echo ""
echo "JSON RESULT:"
echo $output | jq
echo ""
