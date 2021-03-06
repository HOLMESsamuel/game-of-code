# Use the prepared data of the prepare_csv_data notebook to verify that our features are great to use for phishing detection


```python
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix

```

### the events_augmented.csv is generated by the prepare_csv_data notebook


```python
#load the prepared data
events = pd.read_csv("events_augmented.csv")
events.head()

```




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>Unnamed: 0</th>
      <th>index</th>
      <th>id</th>
      <th>date</th>
      <th>attribute_count</th>
      <th>Tag</th>
      <th>header</th>
      <th>from</th>
      <th>feedback_time</th>
      <th>host</th>
      <th>from_count</th>
      <th>host_count</th>
      <th>host_count_ln</th>
      <th>subject</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>0</td>
      <td>19625</td>
      <td>287833</td>
      <td>2022-04-01</td>
      <td>28</td>
      <td>0</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>earleenlpwoodman2593@gmail.com|earleenlpwoodma...</td>
      <td>2022-03-31T23:21:12.000000+0000|2022-03-31T23:...</td>
      <td>www1.nyc.gov|www1.nyc.gov</td>
      <td>2</td>
      <td>2</td>
      <td>0.693147</td>
      <td>Welcome to Your Survey-6668</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>19626</td>
      <td>287834</td>
      <td>2022-04-01</td>
      <td>79</td>
      <td>0</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>quickbooks@notification.intuit.com|quickbooks@...</td>
      <td>2022-04-01T00:26:28.000000+0000|2022-04-01T00:...</td>
      <td>links.notification.intuit.com|links.notificati...</td>
      <td>2</td>
      <td>12</td>
      <td>2.484907</td>
      <td>Invoice 30148805 from Norton Billing</td>
    </tr>
    <tr>
      <th>2</th>
      <td>2</td>
      <td>19627</td>
      <td>287835</td>
      <td>2022-04-01</td>
      <td>89</td>
      <td>1</td>
      <td>Received: from 10.194.205.88\r\n by atlas109.s...</td>
      <td>fortune@msg.fortune.com|fortune@msg.fortune.com</td>
      <td>2022-04-01T00:58:47.000000+0000|2022-04-01T00:...</td>
      <td>fortune.com|fortune.com|fortune.com|fortune.co...</td>
      <td>2</td>
      <td>16</td>
      <td>2.772589</td>
      <td>CBD - The Hottest Thing In 2022 - New Year New...</td>
    </tr>
    <tr>
      <th>3</th>
      <td>3</td>
      <td>19628</td>
      <td>287836</td>
      <td>2022-04-01</td>
      <td>159</td>
      <td>1</td>
      <td>Received: from 10.213.249.35\r\n by atlas106.s...</td>
      <td>miltongrovesc.yahoo.com@send.mailchimpapp.com|...</td>
      <td>2022-04-01T00:55:55.000000+0000|2022-04-01T00:...</td>
      <td>mailchi.mp|miltongrovesc.us15.list-manage.com|...</td>
      <td>2</td>
      <td>30</td>
      <td>3.401197</td>
      <td>Happy Dietday!</td>
    </tr>
    <tr>
      <th>4</th>
      <td>4</td>
      <td>19629</td>
      <td>287837</td>
      <td>2022-04-01</td>
      <td>62</td>
      <td>0</td>
      <td>Received: from 10.194.205.88\r\n by atlas103.s...</td>
      <td>pedidos@riooffsite.com.br|pedidos@riooffsite.c...</td>
      <td>2022-04-01T00:56:38.000000+0000|2022-04-01T00:...</td>
      <td>u10225017.ct.sendgrid.net|usaa.com|u10225017.c...</td>
      <td>2</td>
      <td>10</td>
      <td>2.302585</td>
      <td>Verify mobile number on your profile</td>
    </tr>
  </tbody>
</table>
</div>




```python
events["numbers_in_subject"] = ''

def count_numbers(row):
    return sum(c.isdigit() for c in str(row["subject"]))

events["numbers_in_subject"] = events.apply(lambda row: count_numbers(row), axis=1)

events.head()
```




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>Unnamed: 0</th>
      <th>index</th>
      <th>id</th>
      <th>date</th>
      <th>attribute_count</th>
      <th>Tag</th>
      <th>header</th>
      <th>from</th>
      <th>feedback_time</th>
      <th>host</th>
      <th>from_count</th>
      <th>host_count</th>
      <th>host_count_ln</th>
      <th>subject</th>
      <th>numbers_in_subject</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>0</td>
      <td>19625</td>
      <td>287833</td>
      <td>2022-04-01</td>
      <td>28</td>
      <td>0</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>earleenlpwoodman2593@gmail.com|earleenlpwoodma...</td>
      <td>2022-03-31T23:21:12.000000+0000|2022-03-31T23:...</td>
      <td>www1.nyc.gov|www1.nyc.gov</td>
      <td>2</td>
      <td>2</td>
      <td>0.693147</td>
      <td>Welcome to Your Survey-6668</td>
      <td>4</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>19626</td>
      <td>287834</td>
      <td>2022-04-01</td>
      <td>79</td>
      <td>0</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>quickbooks@notification.intuit.com|quickbooks@...</td>
      <td>2022-04-01T00:26:28.000000+0000|2022-04-01T00:...</td>
      <td>links.notification.intuit.com|links.notificati...</td>
      <td>2</td>
      <td>12</td>
      <td>2.484907</td>
      <td>Invoice 30148805 from Norton Billing</td>
      <td>8</td>
    </tr>
    <tr>
      <th>2</th>
      <td>2</td>
      <td>19627</td>
      <td>287835</td>
      <td>2022-04-01</td>
      <td>89</td>
      <td>1</td>
      <td>Received: from 10.194.205.88\r\n by atlas109.s...</td>
      <td>fortune@msg.fortune.com|fortune@msg.fortune.com</td>
      <td>2022-04-01T00:58:47.000000+0000|2022-04-01T00:...</td>
      <td>fortune.com|fortune.com|fortune.com|fortune.co...</td>
      <td>2</td>
      <td>16</td>
      <td>2.772589</td>
      <td>CBD - The Hottest Thing In 2022 - New Year New...</td>
      <td>4</td>
    </tr>
    <tr>
      <th>3</th>
      <td>3</td>
      <td>19628</td>
      <td>287836</td>
      <td>2022-04-01</td>
      <td>159</td>
      <td>1</td>
      <td>Received: from 10.213.249.35\r\n by atlas106.s...</td>
      <td>miltongrovesc.yahoo.com@send.mailchimpapp.com|...</td>
      <td>2022-04-01T00:55:55.000000+0000|2022-04-01T00:...</td>
      <td>mailchi.mp|miltongrovesc.us15.list-manage.com|...</td>
      <td>2</td>
      <td>30</td>
      <td>3.401197</td>
      <td>Happy Dietday!</td>
      <td>0</td>
    </tr>
    <tr>
      <th>4</th>
      <td>4</td>
      <td>19629</td>
      <td>287837</td>
      <td>2022-04-01</td>
      <td>62</td>
      <td>0</td>
      <td>Received: from 10.194.205.88\r\n by atlas103.s...</td>
      <td>pedidos@riooffsite.com.br|pedidos@riooffsite.c...</td>
      <td>2022-04-01T00:56:38.000000+0000|2022-04-01T00:...</td>
      <td>u10225017.ct.sendgrid.net|usaa.com|u10225017.c...</td>
      <td>2</td>
      <td>10</td>
      <td>2.302585</td>
      <td>Verify mobile number on your profile</td>
      <td>0</td>
    </tr>
  </tbody>
</table>
</div>




```python
events["subject_size"] = ''

def subject_size(row):
    return len(str(row["subject"]))

events["subject_size"] = events.apply(lambda row: subject_size(row), axis=1)

events.head()
```




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>Unnamed: 0</th>
      <th>index</th>
      <th>id</th>
      <th>date</th>
      <th>attribute_count</th>
      <th>Tag</th>
      <th>header</th>
      <th>from</th>
      <th>feedback_time</th>
      <th>host</th>
      <th>from_count</th>
      <th>host_count</th>
      <th>host_count_ln</th>
      <th>subject</th>
      <th>numbers_in_subject</th>
      <th>subject_size</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>0</td>
      <td>19625</td>
      <td>287833</td>
      <td>2022-04-01</td>
      <td>28</td>
      <td>0</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>earleenlpwoodman2593@gmail.com|earleenlpwoodma...</td>
      <td>2022-03-31T23:21:12.000000+0000|2022-03-31T23:...</td>
      <td>www1.nyc.gov|www1.nyc.gov</td>
      <td>2</td>
      <td>2</td>
      <td>0.693147</td>
      <td>Welcome to Your Survey-6668</td>
      <td>4</td>
      <td>27</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>19626</td>
      <td>287834</td>
      <td>2022-04-01</td>
      <td>79</td>
      <td>0</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>quickbooks@notification.intuit.com|quickbooks@...</td>
      <td>2022-04-01T00:26:28.000000+0000|2022-04-01T00:...</td>
      <td>links.notification.intuit.com|links.notificati...</td>
      <td>2</td>
      <td>12</td>
      <td>2.484907</td>
      <td>Invoice 30148805 from Norton Billing</td>
      <td>8</td>
      <td>36</td>
    </tr>
    <tr>
      <th>2</th>
      <td>2</td>
      <td>19627</td>
      <td>287835</td>
      <td>2022-04-01</td>
      <td>89</td>
      <td>1</td>
      <td>Received: from 10.194.205.88\r\n by atlas109.s...</td>
      <td>fortune@msg.fortune.com|fortune@msg.fortune.com</td>
      <td>2022-04-01T00:58:47.000000+0000|2022-04-01T00:...</td>
      <td>fortune.com|fortune.com|fortune.com|fortune.co...</td>
      <td>2</td>
      <td>16</td>
      <td>2.772589</td>
      <td>CBD - The Hottest Thing In 2022 - New Year New...</td>
      <td>4</td>
      <td>68</td>
    </tr>
    <tr>
      <th>3</th>
      <td>3</td>
      <td>19628</td>
      <td>287836</td>
      <td>2022-04-01</td>
      <td>159</td>
      <td>1</td>
      <td>Received: from 10.213.249.35\r\n by atlas106.s...</td>
      <td>miltongrovesc.yahoo.com@send.mailchimpapp.com|...</td>
      <td>2022-04-01T00:55:55.000000+0000|2022-04-01T00:...</td>
      <td>mailchi.mp|miltongrovesc.us15.list-manage.com|...</td>
      <td>2</td>
      <td>30</td>
      <td>3.401197</td>
      <td>Happy Dietday!</td>
      <td>0</td>
      <td>15</td>
    </tr>
    <tr>
      <th>4</th>
      <td>4</td>
      <td>19629</td>
      <td>287837</td>
      <td>2022-04-01</td>
      <td>62</td>
      <td>0</td>
      <td>Received: from 10.194.205.88\r\n by atlas103.s...</td>
      <td>pedidos@riooffsite.com.br|pedidos@riooffsite.c...</td>
      <td>2022-04-01T00:56:38.000000+0000|2022-04-01T00:...</td>
      <td>u10225017.ct.sendgrid.net|usaa.com|u10225017.c...</td>
      <td>2</td>
      <td>10</td>
      <td>2.302585</td>
      <td>Verify mobile number on your profile</td>
      <td>0</td>
      <td>36</td>
    </tr>
  </tbody>
</table>
</div>




```python
events["subject_special_chars"] = ''

def count_special_chars(row):
    return sum((not c.isdigit() and not c.isalpha() and not c.isspace()) for c in str(row["subject"]))

events["subject_special_chars"] = events.apply(lambda row: count_special_chars(row), axis=1)

events.head()
```




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>Unnamed: 0</th>
      <th>index</th>
      <th>id</th>
      <th>date</th>
      <th>attribute_count</th>
      <th>Tag</th>
      <th>header</th>
      <th>from</th>
      <th>feedback_time</th>
      <th>host</th>
      <th>from_count</th>
      <th>host_count</th>
      <th>host_count_ln</th>
      <th>subject</th>
      <th>numbers_in_subject</th>
      <th>subject_size</th>
      <th>subject_special_chars</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>0</td>
      <td>19625</td>
      <td>287833</td>
      <td>2022-04-01</td>
      <td>28</td>
      <td>0</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>earleenlpwoodman2593@gmail.com|earleenlpwoodma...</td>
      <td>2022-03-31T23:21:12.000000+0000|2022-03-31T23:...</td>
      <td>www1.nyc.gov|www1.nyc.gov</td>
      <td>2</td>
      <td>2</td>
      <td>0.693147</td>
      <td>Welcome to Your Survey-6668</td>
      <td>4</td>
      <td>27</td>
      <td>1</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>19626</td>
      <td>287834</td>
      <td>2022-04-01</td>
      <td>79</td>
      <td>0</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>quickbooks@notification.intuit.com|quickbooks@...</td>
      <td>2022-04-01T00:26:28.000000+0000|2022-04-01T00:...</td>
      <td>links.notification.intuit.com|links.notificati...</td>
      <td>2</td>
      <td>12</td>
      <td>2.484907</td>
      <td>Invoice 30148805 from Norton Billing</td>
      <td>8</td>
      <td>36</td>
      <td>0</td>
    </tr>
    <tr>
      <th>2</th>
      <td>2</td>
      <td>19627</td>
      <td>287835</td>
      <td>2022-04-01</td>
      <td>89</td>
      <td>1</td>
      <td>Received: from 10.194.205.88\r\n by atlas109.s...</td>
      <td>fortune@msg.fortune.com|fortune@msg.fortune.com</td>
      <td>2022-04-01T00:58:47.000000+0000|2022-04-01T00:...</td>
      <td>fortune.com|fortune.com|fortune.com|fortune.co...</td>
      <td>2</td>
      <td>16</td>
      <td>2.772589</td>
      <td>CBD - The Hottest Thing In 2022 - New Year New...</td>
      <td>4</td>
      <td>68</td>
      <td>3</td>
    </tr>
    <tr>
      <th>3</th>
      <td>3</td>
      <td>19628</td>
      <td>287836</td>
      <td>2022-04-01</td>
      <td>159</td>
      <td>1</td>
      <td>Received: from 10.213.249.35\r\n by atlas106.s...</td>
      <td>miltongrovesc.yahoo.com@send.mailchimpapp.com|...</td>
      <td>2022-04-01T00:55:55.000000+0000|2022-04-01T00:...</td>
      <td>mailchi.mp|miltongrovesc.us15.list-manage.com|...</td>
      <td>2</td>
      <td>30</td>
      <td>3.401197</td>
      <td>Happy Dietday!</td>
      <td>0</td>
      <td>15</td>
      <td>1</td>
    </tr>
    <tr>
      <th>4</th>
      <td>4</td>
      <td>19629</td>
      <td>287837</td>
      <td>2022-04-01</td>
      <td>62</td>
      <td>0</td>
      <td>Received: from 10.194.205.88\r\n by atlas103.s...</td>
      <td>pedidos@riooffsite.com.br|pedidos@riooffsite.c...</td>
      <td>2022-04-01T00:56:38.000000+0000|2022-04-01T00:...</td>
      <td>u10225017.ct.sendgrid.net|usaa.com|u10225017.c...</td>
      <td>2</td>
      <td>10</td>
      <td>2.302585</td>
      <td>Verify mobile number on your profile</td>
      <td>0</td>
      <td>36</td>
      <td>0</td>
    </tr>
  </tbody>
</table>
</div>



## We use the data that were the more significative in the unsupervised learning notebook


```python
#separate features and prediction
X, y = events[["numbers_in_subject", "subject_size", "subject_special_chars"]], events["Tag"]
print(X.shape)
print(y.shape)
```

    (39323, 3)
    (39323,)
    


```python
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.25, random_state=33)
print (X_train.shape, y_train.shape)
```

    (29492, 3) (29492,)
    


```python
#create linear classification model

from sklearn.linear_model import SGDClassifier
clf = SGDClassifier()
clf.fit(X_train, y_train)
```




<style>#sk-container-id-1 {color: black;background-color: white;}#sk-container-id-1 pre{padding: 0;}#sk-container-id-1 div.sk-toggleable {background-color: white;}#sk-container-id-1 label.sk-toggleable__label {cursor: pointer;display: block;width: 100%;margin-bottom: 0;padding: 0.3em;box-sizing: border-box;text-align: center;}#sk-container-id-1 label.sk-toggleable__label-arrow:before {content: "???";float: left;margin-right: 0.25em;color: #696969;}#sk-container-id-1 label.sk-toggleable__label-arrow:hover:before {color: black;}#sk-container-id-1 div.sk-estimator:hover label.sk-toggleable__label-arrow:before {color: black;}#sk-container-id-1 div.sk-toggleable__content {max-height: 0;max-width: 0;overflow: hidden;text-align: left;background-color: #f0f8ff;}#sk-container-id-1 div.sk-toggleable__content pre {margin: 0.2em;color: black;border-radius: 0.25em;background-color: #f0f8ff;}#sk-container-id-1 input.sk-toggleable__control:checked~div.sk-toggleable__content {max-height: 200px;max-width: 100%;overflow: auto;}#sk-container-id-1 input.sk-toggleable__control:checked~label.sk-toggleable__label-arrow:before {content: "???";}#sk-container-id-1 div.sk-estimator input.sk-toggleable__control:checked~label.sk-toggleable__label {background-color: #d4ebff;}#sk-container-id-1 div.sk-label input.sk-toggleable__control:checked~label.sk-toggleable__label {background-color: #d4ebff;}#sk-container-id-1 input.sk-hidden--visually {border: 0;clip: rect(1px 1px 1px 1px);clip: rect(1px, 1px, 1px, 1px);height: 1px;margin: -1px;overflow: hidden;padding: 0;position: absolute;width: 1px;}#sk-container-id-1 div.sk-estimator {font-family: monospace;background-color: #f0f8ff;border: 1px dotted black;border-radius: 0.25em;box-sizing: border-box;margin-bottom: 0.5em;}#sk-container-id-1 div.sk-estimator:hover {background-color: #d4ebff;}#sk-container-id-1 div.sk-parallel-item::after {content: "";width: 100%;border-bottom: 1px solid gray;flex-grow: 1;}#sk-container-id-1 div.sk-label:hover label.sk-toggleable__label {background-color: #d4ebff;}#sk-container-id-1 div.sk-serial::before {content: "";position: absolute;border-left: 1px solid gray;box-sizing: border-box;top: 0;bottom: 0;left: 50%;z-index: 0;}#sk-container-id-1 div.sk-serial {display: flex;flex-direction: column;align-items: center;background-color: white;padding-right: 0.2em;padding-left: 0.2em;position: relative;}#sk-container-id-1 div.sk-item {position: relative;z-index: 1;}#sk-container-id-1 div.sk-parallel {display: flex;align-items: stretch;justify-content: center;background-color: white;position: relative;}#sk-container-id-1 div.sk-item::before, #sk-container-id-1 div.sk-parallel-item::before {content: "";position: absolute;border-left: 1px solid gray;box-sizing: border-box;top: 0;bottom: 0;left: 50%;z-index: -1;}#sk-container-id-1 div.sk-parallel-item {display: flex;flex-direction: column;z-index: 1;position: relative;background-color: white;}#sk-container-id-1 div.sk-parallel-item:first-child::after {align-self: flex-end;width: 50%;}#sk-container-id-1 div.sk-parallel-item:last-child::after {align-self: flex-start;width: 50%;}#sk-container-id-1 div.sk-parallel-item:only-child::after {width: 0;}#sk-container-id-1 div.sk-dashed-wrapped {border: 1px dashed gray;margin: 0 0.4em 0.5em 0.4em;box-sizing: border-box;padding-bottom: 0.4em;background-color: white;}#sk-container-id-1 div.sk-label label {font-family: monospace;font-weight: bold;display: inline-block;line-height: 1.2em;}#sk-container-id-1 div.sk-label-container {text-align: center;}#sk-container-id-1 div.sk-container {/* jupyter's `normalize.less` sets `[hidden] { display: none; }` but bootstrap.min.css set `[hidden] { display: none !important; }` so we also need the `!important` here to be able to override the default hidden behavior on the sphinx rendered scikit-learn.org. See: https://github.com/scikit-learn/scikit-learn/issues/21755 */display: inline-block !important;position: relative;}#sk-container-id-1 div.sk-text-repr-fallback {display: none;}</style><div id="sk-container-id-1" class="sk-top-container"><div class="sk-text-repr-fallback"><pre>SGDClassifier()</pre><b>In a Jupyter environment, please rerun this cell to show the HTML representation or trust the notebook. <br />On GitHub, the HTML representation is unable to render, please try loading this page with nbviewer.org.</b></div><div class="sk-container" hidden><div class="sk-item"><div class="sk-estimator sk-toggleable"><input class="sk-toggleable__control sk-hidden--visually" id="sk-estimator-id-1" type="checkbox" checked><label for="sk-estimator-id-1" class="sk-toggleable__label sk-toggleable__label-arrow">SGDClassifier</label><div class="sk-toggleable__content"><pre>SGDClassifier()</pre></div></div></div></div></div>




```python
#evaluate on training set
from sklearn import metrics
y_train_pred = clf.predict(X_train)
print (metrics.accuracy_score(y_train, y_train_pred))
```

    0.7357588498575885
    


```python
#evaluate on test set
#Measure accuracy on the testing set
y_pred = clf.predict(X_test)
print (metrics.accuracy_score(y_test, y_pred))
```

    0.7394975078832265
    


```python
#support vector machine classifier
from sklearn import svm
SVM_clf = svm.SVC().fit(X_train, y_train)
SVM_prediction = SVM_clf.predict(X_test)
SVM_accuracy=100*accuracy_score(y_test, SVM_prediction)
print('','Classification Report',classification_report(y_test, SVM_prediction),'',sep='\n'+(55*'=')+'\n')
print('',"Confusion Matrix: ", confusion_matrix(y_test, SVM_prediction),'',sep='\n'+(20*'-')+'\n')
print('',f"Accuracy: {SVM_accuracy}",'',sep='\n'+(50*'*')+'\n')
```

    
    =======================================================
    Classification Report
    =======================================================
                  precision    recall  f1-score   support
    
               0       0.77      1.00      0.87      7540
               1       0.00      0.00      0.00      2291
    
        accuracy                           0.77      9831
       macro avg       0.38      0.50      0.43      9831
    weighted avg       0.59      0.77      0.67      9831
    
    =======================================================
    
    
    --------------------
    Confusion Matrix: 
    --------------------
    [[7540    0]
     [2291    0]]
    --------------------
    
    
    **************************************************
    Accuracy: 76.69616519174042
    **************************************************
    
    

    c:\users\samuel\pycharmprojects\pythonproject\venv\lib\site-packages\sklearn\metrics\_classification.py:1327: UndefinedMetricWarning: Precision and F-score are ill-defined and being set to 0.0 in labels with no predicted samples. Use `zero_division` parameter to control this behavior.
      _warn_prf(average, modifier, msg_start, len(result))
    c:\users\samuel\pycharmprojects\pythonproject\venv\lib\site-packages\sklearn\metrics\_classification.py:1327: UndefinedMetricWarning: Precision and F-score are ill-defined and being set to 0.0 in labels with no predicted samples. Use `zero_division` parameter to control this behavior.
      _warn_prf(average, modifier, msg_start, len(result))
    c:\users\samuel\pycharmprojects\pythonproject\venv\lib\site-packages\sklearn\metrics\_classification.py:1327: UndefinedMetricWarning: Precision and F-score are ill-defined and being set to 0.0 in labels with no predicted samples. Use `zero_division` parameter to control this behavior.
      _warn_prf(average, modifier, msg_start, len(result))
    


```python
from sklearn import neighbors
knn_clf = neighbors.KNeighborsClassifier(n_neighbors=1)
knn_clf.fit(X_train,y_train)
knn_prediction = knn_clf.predict(X_test)
knn_accuracy=100*accuracy_score(y_test, knn_prediction)
print('','Classification Report',classification_report(y_test, knn_prediction),'',sep='\n'+(55*'=')+'\n')
print('',"Confusion Matrix: ", confusion_matrix(y_test, knn_prediction),'',sep='\n'+(20*'-')+'\n')
print('',f"Accuracy: {knn_accuracy}",'',sep='\n'+(50*'*')+'\n')
```

    
    =======================================================
    Classification Report
    =======================================================
                  precision    recall  f1-score   support
    
               0       0.80      0.87      0.83      7540
               1       0.38      0.27      0.32      2291
    
        accuracy                           0.73      9831
       macro avg       0.59      0.57      0.57      9831
    weighted avg       0.70      0.73      0.71      9831
    
    =======================================================
    
    
    --------------------
    Confusion Matrix: 
    --------------------
    [[6552  988]
     [1677  614]]
    --------------------
    
    
    **************************************************
    Accuracy: 72.89187264774692
    **************************************************
    
    


```python
from sklearn.linear_model import LogisticRegression
LR = LogisticRegression()
LR.fit(X_train,y_train)
LR_prediction = LR.predict(X_test)
LR_accuracy=100*accuracy_score(y_test, LR_prediction)
print('','Classification Report',classification_report(y_test, LR_prediction),'',sep='\n'+(55*'=')+'\n')
print('',"Confusion Matrix: ", confusion_matrix(y_test, LR_prediction),'',sep='\n'+(20*'-')+'\n')
print('',f"Accuracy: {LR_accuracy}",'',sep='\n'+(50*'*')+'\n')
```

    
    =======================================================
    Classification Report
    =======================================================
                  precision    recall  f1-score   support
    
               0       0.77      1.00      0.87      7540
               1       0.00      0.00      0.00      2291
    
        accuracy                           0.77      9831
       macro avg       0.38      0.50      0.43      9831
    weighted avg       0.59      0.77      0.67      9831
    
    =======================================================
    
    
    --------------------
    Confusion Matrix: 
    --------------------
    [[7539    1]
     [2291    0]]
    --------------------
    
    
    **************************************************
    Accuracy: 76.68599328654257
    **************************************************
    
    


```python
from sklearn import tree
DT = tree.DecisionTreeClassifier()
DT.fit(X_train, y_train)
DT_prediction = DT.predict(X_test)
DT_accuracy=100.0 * accuracy_score(y_test, DT_prediction)
print('','Classification Report',classification_report(y_test, DT_prediction),'',sep='\n'+(55*'=')+'\n')
print('',"Confusion Matrix: ", confusion_matrix(y_test, DT_prediction),'',sep='\n'+(20*'-')+'\n')
print('',f"Accuracy: {DT_accuracy}",'',sep='\n'+(50*'*')+'\n')
```

    
    =======================================================
    Classification Report
    =======================================================
                  precision    recall  f1-score   support
    
               0       0.79      0.93      0.86      7540
               1       0.48      0.20      0.28      2291
    
        accuracy                           0.76      9831
       macro avg       0.64      0.57      0.57      9831
    weighted avg       0.72      0.76      0.72      9831
    
    =======================================================
    
    
    --------------------
    Confusion Matrix: 
    --------------------
    [[7032  508]
     [1826  465]]
    --------------------
    
    
    **************************************************
    Accuracy: 76.25877326823314
    **************************************************
    
    

## Conclusion 

Our models are unfortunately quite bad at predicting a phishing case, the accuracy is probably this high because there are more non phishing mails
