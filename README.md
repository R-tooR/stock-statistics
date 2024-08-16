# stock-statistics

Project, that allows to asynchronously upload data to calculate some statistics on. 

Computational complexity: $O(b \log_{2}(n))$, where $b$ is size of a batch, and $b << n$. 

Memory complexity: $O(n)$ if data is stored as a double, $O(kn)$ if data is stored as BigDecimal. 
Although BigDecimal ensures absoulte precision, for optimisation purposes application stores data as Doubles, and uses BigDecimals only for calculating average and mean.

Application uses Java 21 and Spring WebFlux.

## Build and run application
It is possible to fetch built container.

`docker pull rtoor12/stock_stats:latest`
`docker run -p 8080:8080 stocks_stats`

### Baremetal

`./gradlew clean build`

`java -jar build/libs/stock-statistics-0.0.1-SNAPSHOT.jar -Xmx4096m -Xms256m -XX:+ZGenerational`

### Docker

`./gradlew clean build`

`docker build -t my-tag .`

`docker run -p 8080:8080 my-tag`

## Interaction
Add some data:

`curl -X POST http://localhost:8080/add_batch -H "Content-Type: application/json" -d '{"symbol": "MCSF", "values": [1.1, 1.4, 1.5, 3.5, 6.7]}'`

Then query for stats

`curl -i -X GET 'http://localhost:8080/stats?symbol=MCSF&k=8'`


## Load test
To check efficiency of the application it's worth to use Locust tool

Install Locust `pip install locust`

Then run `locust --headless --users 10 --spawn-rate 2`

### Results [ms]

#### For app running on baremetal (16 GB memory, 8 CPU)
| Type | Name                                                                                 | 50% | 66% | 75% | 80% | 90% | 95% | 98% | 99% | 99.9% | 99.99% | 100% | # reqs |
|------|--------------------------------------------------------------------------------------|-----|-----|-----|-----|-----|-----|-----|-----|-------|--------|------|--------|
| POST | //add_batch                                                                         | 6   | 6   | 7   | 7   | 8   | 9   | 10  | 11  | 18    | 18     | 18   | 257    |
| GET  | //stats?symbol=AAA&k=8                                                              | 4   | 5   | 6   | 6   | 7   | 8   | 9   | 10  | 11    | 11     | 11   | 275    |
| GET  | //stats?symbol=BBB&k=8                                                              | 5   | 5   | 6   | 6   | 7   | 9   | 10  | 12  | 20    | 20     | 20   | 293    |
| GET  | //stats?symbol=CCC&k=8                                                              | 4   | 5   | 5   | 6   | 7   | 8   | 9   | 10  | 11    | 11     | 11   | 219    |
| GET  | //stats?symbol=DDD&k=8                                                              | 5   | 5   | 6   | 6   | 7   | 8   | 8   | 9   | 12    | 12     | 12   | 272    |
| GET  | //stats?symbol=EEE&k=8                                                              | 4   | 5   | 5   | 6   | 7   | 8   | 9   | 10  | 13    | 13     | 13   | 250    |
| GET  | //stats?symbol=FFF&k=8                                                              | 4   | 5   | 6   | 6   | 7   | 8   | 9   | 10  | 11    | 11     | 11   | 264    |
| GET  | //stats?symbol=GGG&k=8                                                              | 4   | 5   | 5   | 6   | 7   | 8   | 9   | 11  | 33    | 33     | 33   | 278    |
| GET  | //stats?symbol=HHH&k=8                                                              | 4   | 5   | 6   | 6   | 6   | 7   | 9   | 11  | 13    | 13     | 13   | 271    |
| GET  | //stats?symbol=III&k=8                                                              | 4   | 5   | 6   | 6   | 7   | 8   | 9   | 9   | 10    | 10     | 10   | 280    |
| GET  | //stats?symbol=JJJ&k=8                                                              | 4   | 5   | 6   | 6   | 7   | 7   | 9   | 10  | 14    | 14     | 14   | 264    |
| Aggregated |                                                                              | 4   | 5   | 6   | 6   | 7   | 8   | 9   | 10  | 20    | 33     | 33   | 2923   |


#### For app running on container (16 GB memory, 8 CPU)

| Type | Name                                                                                 | 50% | 66% | 75% | 80% | 90% | 95% | 98% | 99% | 99.9% | 99.99% | 100% | # reqs |
|------|--------------------------------------------------------------------------------------|-----|-----|-----|-----|-----|-----|-----|-----|-------|--------|------|--------|
| POST | //add_batch                                                                         | 6   | 7   | 8   | 8   | 9   | 11  | 13  | 13  | 190   | 190    | 190  | 287    |
| GET  | //stats?symbol=AAA&k=8                                                              | 5   | 6   | 7   | 7   | 8   | 8   | 9   | 12  | 15    | 15     | 15   | 265    |
| GET  | //stats?symbol=BBB&k=8                                                              | 5   | 6   | 7   | 7   | 8   | 9   | 10  | 11  | 14    | 14     | 14   | 260    |
| GET  | //stats?symbol=CCC&k=8                                                              | 5   | 6   | 7   | 7   | 8   | 9   | 10  | 11  | 25    | 25     | 25   | 313    |
| GET  | //stats?symbol=DDD&k=8                                                              | 5   | 6   | 7   | 7   | 9   | 10  | 13  | 19  | 45    | 45     | 45   | 255    |
| GET  | //stats?symbol=EEE&k=8                                                              | 5   | 6   | 6   | 7   | 8   | 8   | 11  | 12  | 21    | 21     | 21   | 290    |
| GET  | //stats?symbol=FFF&k=8                                                              | 5   | 6   | 7   | 7   | 8   | 9   | 10  | 12  | 130   | 130    | 130  | 257    |
| GET  | //stats?symbol=GGG&k=8                                                              | 5   | 6   | 6   | 7   | 8   | 10  | 11  | 12  | 13    | 13     | 13   | 273    |
| GET  | //stats?symbol=HHH&k=8                                                              | 5   | 6   | 6   | 7   | 9   | 10  | 12  | 13  | 24    | 24     | 24   | 269    |
| GET  | //stats?symbol=III&k=8                                                              | 5   | 6   | 6   | 7   | 8   | 9   | 11  | 11  | 15    | 15     | 15   | 274    |
| GET  | //stats?symbol=JJJ&k=8                                                              | 5   | 6   | 7   | 7   | 8   | 9   | 11  | 11  | 17    | 17     | 17   | 248    |
|      | Aggregated                                                                          | 5   | 6   | 7   | 7   | 8   | 9   | 11  | 13  | 54    | 190    | 190  | 2991   |

