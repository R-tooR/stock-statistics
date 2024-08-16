import random

from locust import HttpUser, between, task


def generate_random_doubles(count, lower_bound=0.0, upper_bound=100.0):
    return [random.uniform(lower_bound, upper_bound) for _ in range(count)]


class User(HttpUser):
    #wait_time = between(1, 5)
    wait_time = between(0.1, 0.5)
    host = 'http://localhost:8080/'
    recent_etags = {
        'AAA': "EMPTY",
        'BBB': "EMPTY",
        'CCC': "EMPTY",
        'DDD': "EMPTY",
        'EEE': "EMPTY",
        'FFF': "EMPTY",
        'GGG': "EMPTY",
        'HHH': "EMPTY",
        'III': "EMPTY",
        'JJJ': "EMPTY"
    }

    @task
    def post(self):
        json_body = self._get_random_symbol_and_batch()
        self.client.post(url="/add_batch", json=json_body,
                         headers={"Content-Type": "application/json"})

    @task(10)
    def get(self):
        symbol = self._symbols[random.randint(0, 9)]
        #k = random.randint(1, 8)
        k = 8
        headers = {
            'If-None-Match' : self.recent_etags[symbol]
        }
        result = self.client.get(f"/stats?symbol={symbol}&k={k}", headers=headers)
        etag = result.headers.get("ETag")
        self.recent_etags[symbol] = etag



    _symbols = ['AAA', 'BBB', 'CCC', 'DDD', 'EEE', 'FFF', 'GGG', 'HHH', 'III', 'JJJ']
    #_symbols = ['AAA', 'BBB']
    def _get_random_symbol_and_batch(self):
        return {
            "symbol": self._symbols[random.randint(0, 9)],
            "values": generate_random_doubles(random.randint(20, 50))
        }

