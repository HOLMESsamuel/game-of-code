# Feature determination with unsupervised learning scoring


```python
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
```

### the input with body dataset has been prepared from a part of the 262Go dataset by a piece of java code to exctract the mail characteristics and return a csv file to be exploited here


```python
df = pd.read_csv("input_with_body.csv")
df.head()
print(df.shape)
```

    (5004, 9)
    


```python
df.keys()
```




    Index(['id', 'from', 'to', 'cc', 'subject', 'body', 'date', 'reply',
           'references'],
          dtype='object')




```python
#the percentage of null value per column
round(df.isnull().sum()*100/df.shape[0],2).sort_values(ascending=False).head(20)

```




    references    100.00
    reply          61.15
    cc             56.12
    body            9.97
    subject         2.72
    to              0.14
    id              0.00
    from            0.00
    date            0.00
    dtype: float64



# We create  numerical features from non numerical columns

### The number of special chars in the body


```python
df["body_special_chars"] = ''

def body_special_chars(row):
    return sum((not c.isdigit() and not c.isalpha() and not c.isspace()) for c in str(row["body"]))

df["body_special_chars"] = df.apply(lambda row: body_special_chars(row), axis=1)

```


```python
df["body_size"] = ''

def body_size(row):
    return len(str(row["body"]))

df["body_size"] = df.apply(lambda row: body_size(row), axis=1)
```


```python
df["cc_count"] = ''
df.head()
def count_cc(row):
    if str(row["cc"]) == "nan":
        return 0
    else:
        return len(str(row["cc"]).split("|"))

df["cc_count"] = df.apply(lambda row: count_cc(row), axis=1)
```


```python
df["from_count"] = ''

def count_from(row):
        return len(str(row["from"]).split("|"))

df["from_count"] = df.apply(lambda row: count_from(row), axis=1)
```


```python
df["to_count"] = ''

def count_to(row):
        return len(str(row["to"]).split("|"))

df["to_count"] = df.apply(lambda row: count_to(row), axis=1)
```


```python
df["numbers_in_subject"] = ''

def count_numbers(row):
    return sum(c.isdigit() for c in str(row["subject"]))

df["numbers_in_subject"] = df.apply(lambda row: count_numbers(row), axis=1)
```


```python
df["subject_size"] = ''

def subject_size(row):
    return len(str(row["subject"]))

df["subject_size"] = df.apply(lambda row: subject_size(row), axis=1)

```


```python
df["subject_special_chars"] = ''

def count_special_chars(row):
    return sum((not c.isdigit() and not c.isalpha() and not c.isspace()) for c in str(row["subject"]))

df["subject_special_chars"] = df.apply(lambda row: count_special_chars(row), axis=1)

df.head()
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
      <th>id</th>
      <th>from</th>
      <th>to</th>
      <th>cc</th>
      <th>subject</th>
      <th>body</th>
      <th>date</th>
      <th>reply</th>
      <th>references</th>
      <th>body_special_chars</th>
      <th>body_size</th>
      <th>cc_count</th>
      <th>from_count</th>
      <th>to_count</th>
      <th>numbers_in_subject</th>
      <th>subject_size</th>
      <th>subject_special_chars</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>1</td>
      <td>&lt;docs@sawatzky.ru&gt;</td>
      <td>&lt;a.anishchenko@sawatzky.ru&gt;</td>
      <td>NaN</td>
      <td>Требуется Ваше согласование заявки на опл��ту ...</td>
      <td>NaN</td>
      <td>Fri| 1 Apr 2022 10:07:02 +0300</td>
      <td>NaN</td>
      <td>NaN</td>
      <td>0</td>
      <td>3</td>
      <td>0</td>
      <td>1</td>
      <td>1</td>
      <td>10</td>
      <td>66</td>
      <td>6</td>
    </tr>
    <tr>
      <th>1</th>
      <td>2</td>
      <td>Gorbey Viacheslav &lt;v.gorbey@sawatzky.ru&gt;</td>
      <td>Kulakov Dmitry &lt;dk@sawatzky.ru&gt;| Kalinovsky Ro...</td>
      <td>Anishenko Aleksandr &lt;a.anishchenko@sawatzky.ru...</td>
      <td>Отчет БЦ Павловский 01.04.2022 г.</td>
      <td>Уважаемые коллеги добрый день!Название объекта...</td>
      <td>Fri| 1 Apr 2022 06:52:22 +0000</td>
      <td>NaN</td>
      <td>NaN</td>
      <td>75</td>
      <td>933</td>
      <td>2</td>
      <td>1</td>
      <td>2</td>
      <td>8</td>
      <td>34</td>
      <td>3</td>
    </tr>
    <tr>
      <th>2</th>
      <td>3</td>
      <td>Gorbey Viacheslav &lt;v.gorbey@sawatzky.ru&gt;</td>
      <td>Roman Vinogradov VTV GROUP LLC &lt;vinogradov@vtv...</td>
      <td>vandakurova@vtv-group.ru &lt;vandakurova@vtv-grou...</td>
      <td>PV изменение объема услуг</td>
      <td>Роман добрый день!В связи с изменением конъюнк...</td>
      <td>Thu| 31 Mar 2022 12:11:02 +0000</td>
      <td>NaN</td>
      <td>NaN</td>
      <td>36</td>
      <td>548</td>
      <td>3</td>
      <td>1</td>
      <td>1</td>
      <td>0</td>
      <td>25</td>
      <td>0</td>
    </tr>
    <tr>
      <th>3</th>
      <td>4</td>
      <td>&lt;docs@sawatzky.ru&gt;</td>
      <td>&lt;a.anishchenko@sawatzky.ru&gt;</td>
      <td>NaN</td>
      <td>Требуется Ваше согласование электронной служеб...</td>
      <td>NaN</td>
      <td>Thu| 24 Mar 2022 14:15:12 +0300</td>
      <td>NaN</td>
      <td>NaN</td>
      <td>0</td>
      <td>3</td>
      <td>0</td>
      <td>1</td>
      <td>1</td>
      <td>9</td>
      <td>86</td>
      <td>5</td>
    </tr>
    <tr>
      <th>4</th>
      <td>5</td>
      <td>corp.autoreply@ntvplus.com &lt;corp.autoreply@ntv...</td>
      <td>Anishenko Aleksandr &lt;a.anishchenko@sawatzky.ru&gt;</td>
      <td>NaN</td>
      <td>Автоматический ответ: обновление модулей НТВ-ПЛЮС</td>
      <td>Уважаемый абонент!Благодарим Вас за обращение ...</td>
      <td>Fri| 11 Feb 2022 09:52:00 +0000</td>
      <td>&lt;FCF221A88169C846A25ADF5C38FC168291DD1ECA@NTV-...</td>
      <td>NaN</td>
      <td>18</td>
      <td>378</td>
      <td>0</td>
      <td>1</td>
      <td>1</td>
      <td>0</td>
      <td>49</td>
      <td>2</td>
    </tr>
  </tbody>
</table>
</div>




```python
###we extract and normalize numerical features
numerical_features = df[["body_size","cc_count", "numbers_in_subject", "from_count","to_count","subject_size", "subject_special_chars", "body_special_chars"]]
numerical_features.head()
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
      <th>body_size</th>
      <th>cc_count</th>
      <th>numbers_in_subject</th>
      <th>from_count</th>
      <th>to_count</th>
      <th>subject_size</th>
      <th>subject_special_chars</th>
      <th>body_special_chars</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>3</td>
      <td>0</td>
      <td>10</td>
      <td>1</td>
      <td>1</td>
      <td>66</td>
      <td>6</td>
      <td>0</td>
    </tr>
    <tr>
      <th>1</th>
      <td>933</td>
      <td>2</td>
      <td>8</td>
      <td>1</td>
      <td>2</td>
      <td>34</td>
      <td>3</td>
      <td>75</td>
    </tr>
    <tr>
      <th>2</th>
      <td>548</td>
      <td>3</td>
      <td>0</td>
      <td>1</td>
      <td>1</td>
      <td>25</td>
      <td>0</td>
      <td>36</td>
    </tr>
    <tr>
      <th>3</th>
      <td>3</td>
      <td>0</td>
      <td>9</td>
      <td>1</td>
      <td>1</td>
      <td>86</td>
      <td>5</td>
      <td>0</td>
    </tr>
    <tr>
      <th>4</th>
      <td>378</td>
      <td>0</td>
      <td>0</td>
      <td>1</td>
      <td>1</td>
      <td>49</td>
      <td>2</td>
      <td>18</td>
    </tr>
  </tbody>
</table>
</div>



### We normalize the data to fit a Kmeans model


```python
numerical_features = (numerical_features - numerical_features.mean()) / (numerical_features.max() - numerical_features.min())
numerical_features.head()
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
      <th>body_size</th>
      <th>cc_count</th>
      <th>numbers_in_subject</th>
      <th>from_count</th>
      <th>to_count</th>
      <th>subject_size</th>
      <th>subject_special_chars</th>
      <th>body_special_chars</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>-0.034547</td>
      <td>-0.016207</td>
      <td>0.237772</td>
      <td>-0.001599</td>
      <td>-0.002220</td>
      <td>0.168855</td>
      <td>0.318284</td>
      <td>-0.031721</td>
    </tr>
    <tr>
      <th>1</th>
      <td>-0.022073</td>
      <td>0.005771</td>
      <td>0.175272</td>
      <td>-0.001599</td>
      <td>-0.001324</td>
      <td>-0.009916</td>
      <td>0.087515</td>
      <td>-0.022733</td>
    </tr>
    <tr>
      <th>2</th>
      <td>-0.027237</td>
      <td>0.016760</td>
      <td>-0.074728</td>
      <td>-0.001599</td>
      <td>-0.002220</td>
      <td>-0.060195</td>
      <td>-0.143255</td>
      <td>-0.027407</td>
    </tr>
    <tr>
      <th>3</th>
      <td>-0.034547</td>
      <td>-0.016207</td>
      <td>0.206522</td>
      <td>-0.001599</td>
      <td>-0.002220</td>
      <td>0.280587</td>
      <td>0.241361</td>
      <td>-0.031721</td>
    </tr>
    <tr>
      <th>4</th>
      <td>-0.029517</td>
      <td>-0.016207</td>
      <td>-0.074728</td>
      <td>-0.001599</td>
      <td>-0.002220</td>
      <td>0.073883</td>
      <td>0.010592</td>
      <td>-0.029564</td>
    </tr>
  </tbody>
</table>
</div>



### We use a kmeans model to clusterize our mails, trying from 2 to 5 clusters


```python
from sklearn.cluster import KMeans
from sklearn import metrics

def clustering_algorithm(n_clusters, dataset):
    kmeans = KMeans(n_clusters=n_clusters, n_init=10, max_iter=300)
    labels = kmeans.fit_predict(dataset)
    silhouette = metrics.silhouette_score(dataset, labels, metric='euclidean')
    return silhouette

for i in range(2, 5):
    silhouette = clustering_algorithm(i, numerical_features)
    print(i, silhouette)

#The best score is obtained for 2 clusters
```

    2 0.5403374190422666
    3 0.38260960549162476
    4 0.42529670345944115
    


```python
#The best result is obtained for 2 clusters we use the silhouette metric to measure it
#We can evaluate our model by evaluating it versus random data

random_data = np.random.rand(167,9)
silhouette_random = clustering_algorithm(2, random_data)
silhouette = clustering_algorithm(2, numerical_features)

print(silhouette_random)
print(silhouette)
```

    0.09780890782852719
    0.5403374190422666
    


```python
import seaborn as sns
kmeans = KMeans(n_clusters=2, n_init=10, max_iter=300)
y_pred = kmeans.fit_predict(numerical_features)
labels = kmeans.labels_

df['labels'] = labels
sns.catplot(x='labels', kind='count', data=df)

```




    <seaborn.axisgrid.FacetGrid at 0x283d0b486d0>




    
![png](output_21_1.png)
    



```python
centroids = kmeans.cluster_centers_
for i in range(len(centroids[0])):
    print(numerical_features.columns.values[i],"\n{:.4f}".format(centroids[:, i].var()))
```

    body_size 
    0.0000
    cc_count 
    0.0000
    numbers_in_subject 
    0.0153
    from_count 
    0.0000
    to_count 
    0.0000
    subject_size 
    0.0153
    subject_special_chars 
    0.0281
    body_special_chars 
    0.0001
    

#### the most used features seems to be the number of special characters and the number of numerical characters in the subject


```python
df_0 = df[df['labels'] == 0]
df_1 = df[df['labels'] == 1]
```

# We visualize how well the model separates the data in 2 clusters


```python
plt.figure(figsize=(8, 8))
plt.scatter(df_0['subject_special_chars'], df_0['numbers_in_subject'], c='blue', s=10, label='A')
plt.scatter(df_1['subject_special_chars'], df_1['numbers_in_subject'], c='red', s=10, label='B')
plt.xlabel('subject special chars')
plt.ylabel('number characters in subject')
plt.legend()
plt.show
```




    <function matplotlib.pyplot.show(close=None, block=None)>




    
![png](output_26_1.png)
    



```python
plt.figure(figsize=(8, 8))
plt.scatter(df_0['subject_special_chars'], df_0['body_special_chars'], c='blue', s=10, label='A')
plt.scatter(df_1['subject_special_chars'], df_1['body_special_chars'], c='red', s=10, label='B')
plt.xlabel('subject number of special chars')
plt.ylabel('body special chars')
plt.legend()
plt.show
```




    <function matplotlib.pyplot.show(close=None, block=None)>




    
![png](output_27_1.png)
    


## Conclusion

In this notebook we exploited the csv file of a dataset of mail, we manufactured some numerical features that we judged useful
and tried to separate the data with a kmeans clustering model. The separation seams to work and we identified some features that separates
the dataset better.
In the next notebook we will validate our hypothesis that this features can identify  phishing email with classic supervised learning algorithms
