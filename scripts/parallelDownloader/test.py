import os
import requests
from multiprocessing.pool import ThreadPool

url_to_fetch = []
base_dir = '/home/ubuntu/Documents/upwork/RDF/parallelDownloads/rawContent/'
with open("urls") as f:
    content = f.readlines()
    for c in content:
        filename = c.split(" ")[0].split("/")[-1].replace(">","")
        u = c.split(" ")[2].replace('"','')
        url_to_fetch.append((base_dir+filename,u))

for filename,u in url_to_fetch:
    print u

def fetch_url(entry):
    path, uri = entry
    if not os.path.exists(path):
        r = requests.get(uri, stream=True)
        if r.status_code == 200:
            with open(path, 'wb') as f:
                for chunk in r:
                    f.write(chunk)
    return path

results = ThreadPool(32).imap_unordered(fetch_url, url_to_fetch)
for path in results:
    print(path)
