# Import the Observatory top shared csv and prepare it to be used in cross validation notebook


```python
import gc

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
```


```python
attributes_1 = pd.read_csv("_Spambee_Attributes__2022-04.csv")
attributes_2 = pd.read_csv("_Spambee_Attributes__2022-05.csv")
events_1 = pd.read_csv("_Spambee_Events__2022-04.csv")
events_2 = pd.read_csv("_Spambee_Events__2022-05.csv")
print(attributes_1.shape)
print(attributes_2.shape)
print(events_1.shape)
print(events_2.shape)
```

    (220418, 5)
    (237924, 5)
    (19698, 5)
    (19625, 5)
    


```python
attributes = pd.concat([attributes_1, attributes_2], ignore_index=True)
events = pd.concat([events_1, events_2], ignore_index=True)

del attributes_1
del attributes_2
del events_1
del events_2

print(attributes.shape)
print(events.shape)
```

    (458342, 5)
    (39323, 5)
    


```python
events["header"] = ''
events["from"] = ''
events["feedback_time"] = ''
events["host"] = ''
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
      <th>index</th>
      <th>id</th>
      <th>date</th>
      <th>attribute_count</th>
      <th>Tag</th>
      <th>header</th>
      <th>from</th>
      <th>feedback_time</th>
      <th>host</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>19625</td>
      <td>287833</td>
      <td>2022-04-01</td>
      <td>28</td>
      <td>None</td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <th>1</th>
      <td>19626</td>
      <td>287834</td>
      <td>2022-04-01</td>
      <td>79</td>
      <td>None</td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <th>2</th>
      <td>19627</td>
      <td>287835</td>
      <td>2022-04-01</td>
      <td>89</td>
      <td>Phishing</td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <th>3</th>
      <td>19628</td>
      <td>287836</td>
      <td>2022-04-01</td>
      <td>159</td>
      <td>Phishing</td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <th>4</th>
      <td>19629</td>
      <td>287837</td>
      <td>2022-04-01</td>
      <td>62</td>
      <td>None</td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
    </tr>
  </tbody>
</table>
</div>



## Merge the two different table to mix attributes with events joined on event_id


```python
i=0
for event_id in events["id"]:
        event_attributes = attributes.loc[attributes["event_id"] == event_id]
        headers = event_attributes.loc[event_attributes["object_relation"] == "header", "value"]
        from_attribute = event_attributes.loc[event_attributes["object_relation"] == "from", "value"]
        feedback_time = event_attributes.loc[event_attributes["object_relation"] == "feedback_time", "value"]
        host = event_attributes.loc[event_attributes["object_relation"] == "host", "value"]
        events["header"][i] = ''.join(headers)
        events["from"][i] = '|'.join(from_attribute)
        events["feedback_time"][i] = '|'.join(feedback_time)
        events["host"][i] = '|'.join(host)
        i += 1

events.head()
```

    C:\Users\Samuel\AppData\Local\Temp\ipykernel_20196\3642184332.py:8: SettingWithCopyWarning: 
    A value is trying to be set on a copy of a slice from a DataFrame
    
    See the caveats in the documentation: https://pandas.pydata.org/pandas-docs/stable/user_guide/indexing.html#returning-a-view-versus-a-copy
      events["header"][i] = ''.join(headers)
    C:\Users\Samuel\AppData\Local\Temp\ipykernel_20196\3642184332.py:9: SettingWithCopyWarning: 
    A value is trying to be set on a copy of a slice from a DataFrame
    
    See the caveats in the documentation: https://pandas.pydata.org/pandas-docs/stable/user_guide/indexing.html#returning-a-view-versus-a-copy
      events["from"][i] = '|'.join(from_attribute)
    C:\Users\Samuel\AppData\Local\Temp\ipykernel_20196\3642184332.py:10: SettingWithCopyWarning: 
    A value is trying to be set on a copy of a slice from a DataFrame
    
    See the caveats in the documentation: https://pandas.pydata.org/pandas-docs/stable/user_guide/indexing.html#returning-a-view-versus-a-copy
      events["feedback_time"][i] = '|'.join(feedback_time)
    C:\Users\Samuel\AppData\Local\Temp\ipykernel_20196\3642184332.py:11: SettingWithCopyWarning: 
    A value is trying to be set on a copy of a slice from a DataFrame
    
    See the caveats in the documentation: https://pandas.pydata.org/pandas-docs/stable/user_guide/indexing.html#returning-a-view-versus-a-copy
      events["host"][i] = '|'.join(host)
    




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
      <th>index</th>
      <th>id</th>
      <th>date</th>
      <th>attribute_count</th>
      <th>Tag</th>
      <th>header</th>
      <th>from</th>
      <th>feedback_time</th>
      <th>host</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>19625</td>
      <td>287833</td>
      <td>2022-04-01</td>
      <td>28</td>
      <td>None</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>earleenlpwoodman2593@gmail.com|earleenlpwoodma...</td>
      <td>2022-03-31T23:21:12.000000+0000|2022-03-31T23:...</td>
      <td>www1.nyc.gov|www1.nyc.gov</td>
    </tr>
    <tr>
      <th>1</th>
      <td>19626</td>
      <td>287834</td>
      <td>2022-04-01</td>
      <td>79</td>
      <td>None</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>quickbooks@notification.intuit.com|quickbooks@...</td>
      <td>2022-04-01T00:26:28.000000+0000|2022-04-01T00:...</td>
      <td>links.notification.intuit.com|links.notificati...</td>
    </tr>
    <tr>
      <th>2</th>
      <td>19627</td>
      <td>287835</td>
      <td>2022-04-01</td>
      <td>89</td>
      <td>Phishing</td>
      <td>Received: from 10.194.205.88\r\n by atlas109.s...</td>
      <td>fortune@msg.fortune.com|fortune@msg.fortune.com</td>
      <td>2022-04-01T00:58:47.000000+0000|2022-04-01T00:...</td>
      <td>fortune.com|fortune.com|fortune.com|fortune.co...</td>
    </tr>
    <tr>
      <th>3</th>
      <td>19628</td>
      <td>287836</td>
      <td>2022-04-01</td>
      <td>159</td>
      <td>Phishing</td>
      <td>Received: from 10.213.249.35\r\n by atlas106.s...</td>
      <td>miltongrovesc.yahoo.com@send.mailchimpapp.com|...</td>
      <td>2022-04-01T00:55:55.000000+0000|2022-04-01T00:...</td>
      <td>mailchi.mp|miltongrovesc.us15.list-manage.com|...</td>
    </tr>
    <tr>
      <th>4</th>
      <td>19629</td>
      <td>287837</td>
      <td>2022-04-01</td>
      <td>62</td>
      <td>None</td>
      <td>Received: from 10.194.205.88\r\n by atlas103.s...</td>
      <td>pedidos@riooffsite.com.br|pedidos@riooffsite.c...</td>
      <td>2022-04-01T00:56:38.000000+0000|2022-04-01T00:...</td>
      <td>u10225017.ct.sendgrid.net|usaa.com|u10225017.c...</td>
    </tr>
  </tbody>
</table>
</div>




```python
events.to_csv("events.csv")
```


```python
#replace tag by numerical value (0 for none 1 for phishing)
events.loc[events["Tag"] == "None", "Tag"] = 0
events.loc[events["Tag"] == "Phishing", "Tag"] = 1
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
      <th>index</th>
      <th>id</th>
      <th>date</th>
      <th>attribute_count</th>
      <th>Tag</th>
      <th>header</th>
      <th>from</th>
      <th>feedback_time</th>
      <th>host</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>19625</td>
      <td>287833</td>
      <td>2022-04-01</td>
      <td>28</td>
      <td>0</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>earleenlpwoodman2593@gmail.com|earleenlpwoodma...</td>
      <td>2022-03-31T23:21:12.000000+0000|2022-03-31T23:...</td>
      <td>www1.nyc.gov|www1.nyc.gov</td>
    </tr>
    <tr>
      <th>1</th>
      <td>19626</td>
      <td>287834</td>
      <td>2022-04-01</td>
      <td>79</td>
      <td>0</td>
      <td>Delivered-To: UNDISCLOSEDFORPRIVACY@PRIVACYDOM...</td>
      <td>quickbooks@notification.intuit.com|quickbooks@...</td>
      <td>2022-04-01T00:26:28.000000+0000|2022-04-01T00:...</td>
      <td>links.notification.intuit.com|links.notificati...</td>
    </tr>
    <tr>
      <th>2</th>
      <td>19627</td>
      <td>287835</td>
      <td>2022-04-01</td>
      <td>89</td>
      <td>1</td>
      <td>Received: from 10.194.205.88\r\n by atlas109.s...</td>
      <td>fortune@msg.fortune.com|fortune@msg.fortune.com</td>
      <td>2022-04-01T00:58:47.000000+0000|2022-04-01T00:...</td>
      <td>fortune.com|fortune.com|fortune.com|fortune.co...</td>
    </tr>
    <tr>
      <th>3</th>
      <td>19628</td>
      <td>287836</td>
      <td>2022-04-01</td>
      <td>159</td>
      <td>1</td>
      <td>Received: from 10.213.249.35\r\n by atlas106.s...</td>
      <td>miltongrovesc.yahoo.com@send.mailchimpapp.com|...</td>
      <td>2022-04-01T00:55:55.000000+0000|2022-04-01T00:...</td>
      <td>mailchi.mp|miltongrovesc.us15.list-manage.com|...</td>
    </tr>
    <tr>
      <th>4</th>
      <td>19629</td>
      <td>287837</td>
      <td>2022-04-01</td>
      <td>62</td>
      <td>0</td>
      <td>Received: from 10.194.205.88\r\n by atlas103.s...</td>
      <td>pedidos@riooffsite.com.br|pedidos@riooffsite.c...</td>
      <td>2022-04-01T00:56:38.000000+0000|2022-04-01T00:...</td>
      <td>u10225017.ct.sendgrid.net|usaa.com|u10225017.c...</td>
    </tr>
  </tbody>
</table>
</div>




```python
def count_from(row):
        return len(row["from"].split("|"))

events["from_count"] = events.apply(lambda row: count_from(row), axis=1)

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
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
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
    </tr>
    <tr>
      <th>1</th>
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
    </tr>
    <tr>
      <th>2</th>
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
    </tr>
    <tr>
      <th>3</th>
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
    </tr>
    <tr>
      <th>4</th>
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
    </tr>
  </tbody>
</table>
</div>




```python
events["from_count"].unique()
```




    array([2, 1, 3, 4], dtype=int64)




```python
def count_host(row):
        return len(row["host"].split("|"))

events["host_count"] = events.apply(lambda row: count_host(row), axis=1)

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
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
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
    </tr>
    <tr>
      <th>1</th>
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
    </tr>
    <tr>
      <th>2</th>
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
    </tr>
    <tr>
      <th>3</th>
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
    </tr>
    <tr>
      <th>4</th>
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
    </tr>
  </tbody>
</table>
</div>




```python
events["host_count"].unique()
```




    array([  2,  12,  16,  30,  10,  24,  14,   4,   6, 206,   1,   8,  32,
            46,  96, 148,  20,  42,  22,  48,  40, 332,  72, 142,  70,  18,
             5,  34, 106, 144,  64,  50, 146,  26, 238,  28,  36,  44,  68,
           152,  56, 116, 202, 154, 143, 112,   3,  37,   9,  73,  23,   7,
            39,  11,  13,  27,  15,  17,  35,  19, 140,  33,  52,  38, 108,
            55,  88,  67,  91,  29,  53,  21,  25, 124,  45, 115, 132,  31,
            75,  63,  65,  43,  78,  66,  58, 120, 147,  54,  84, 135,  57,
           105,  51,  99, 165,  69,  81, 129, 138,  90,  98,  62, 190, 103,
            86,  60,  82, 104, 100, 102, 216,  94,  76,  71,  93, 114, 188,
           122, 118, 208, 126, 164,  80, 150,  74,  61,  41, 145,  47, 125,
           184, 186, 171, 153, 231, 210, 189, 240, 111, 200,  87, 201, 177,
           162, 141, 496, 212, 169, 285, 160,  92, 121, 159, 117, 192, 315,
           258, 195,  49,  77, 421,  59, 361, 127, 123, 128, 244, 276, 174,
           134, 232, 228, 178, 220, 130, 587, 349,  79, 209,  97, 158,  95,
            89, 168, 722, 180, 136, 167, 173, 151, 324, 109, 101, 163, 113,
           119,  83, 110, 301, 397, 481, 166,  85, 107, 311, 385, 389, 155,
           139, 156, 170, 137, 493, 313, 337], dtype=int64)




```python
import numpy as np

events["host_count_ln"] = events.apply(lambda row: np.log(row["host_count"]), axis=1)

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
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
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
    </tr>
    <tr>
      <th>1</th>
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
    </tr>
    <tr>
      <th>2</th>
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
    </tr>
    <tr>
      <th>3</th>
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
    </tr>
    <tr>
      <th>4</th>
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
    </tr>
  </tbody>
</table>
</div>




```python
#use a regex to recover the subject of the mail from the header

import re

regex = re.compile(r'(?<=Subject: )(.*)(?=\n)')
events["subject"] = events.apply(lambda row: regex.search(row["header"]).group() if regex.search(row["header"]) is not None else "", axis=1)

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
events.to_csv("events_augmented.csv")

```
