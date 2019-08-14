# Benchmarker
A tool to efficiently test cloud resource configurations for distributed applications

## Useful Commands
* `make` recompiles Java code and uploads the latest versions of Docker images to Docker Hub
* `make prom-mini-up` and `make prom-mini-clean` are used to control the Prometheus addon for MiniShift
* `make clean-prom` and `make up-prom` do the same thing for OpenShift
* `make clean-all` and `make up-all` should be used after making changes to pods that stay up between experimental runs
* `make clean` and `make up` run an experiment, but if you want to see the output, consider using a Python script instead
* `python experiment.py` runs a MiniShift experiment (or a series or experiments)
* `python plot_experiment.py` generates Matplotlib plots from the data retrieved by `experiment.py`

## Directories and Files
* `config`
  * `components.yaml`: a configuration file that lays out a chain of components and their resource usage (used automatically)
  * `flink-conf.yaml`: the configuration file for Flink (added to the Docker image)
  * `global_periodic.yaml`: a different version of `global.yaml`, one with strictly periodic workload
  * `global.yaml`: all other configurable parameters (used automatically)
  * `ml_components.yaml`: an example `components.yaml` file that mimics a machine learning system (I/O is optional)
  * `prometheus.yml`: the configuration file for Prometheus (used manually)
* `data`: each file records a single experiment. Each filename has four parts:
  1. what performance metric was measured (`cpu`, `heap`, or `throughput`)
  2. the expected amount of memory (64, 128, 256, or 512 MiB)
  3. the size of the output string (1, 2, 4, ..., 256 MiB)
  4. run ID (in case there are multiple runs with the same parameters)
* `docker`: all files needed to set up Docker Compose and/or OpenShift
  * `docker-compose.yml`: a Docker Compose file that was used as a basis for the OpenShift setup
  * `Dockerfile.bench`: a Dockerfile for the Flink JobManagers and TaskManagers
  * `Dockerfile.control`: a Dockerfile for the control server
  * `Dockerfile.prom`: a Dockerfile for Prometheus
  * `Dockerfile.start`: a Dockerfile that starts the Flink application
  * `openshift`: OpenShift manifestos generated using Kompose (with some modifications and additions)
* `experiment.py`: the primary way to run a MiniShift experiment and get Prometheus data to a local directory
* `local_experiments`: experiments with standalone Java applications similar to the main Component class
  * `io_memory_tests`: does the I/O system use the right amount of memory? Barely.
  * `io_runtime_tests`: does the I/O system take the right amount of time? Usually.
    * `linkedlist*` and `RandomList.java`: how long does it take to add a random number to a linked list?
  * `memory_tests`: does the main Component class use the right amount of memory? Yes!
  * `analysis*.R`: data analysis and plots using R
  * `Component.java`: versions of the original Component class adapted to be run without Flink
  * `experiment*.py`: Python scripts that run Component with various parameters and capture its performance in a CSV file
  * `FullComponent.java`: similar to Component.java, but used to check the performance after adjusting the constants
  * `plots` and `*.png`: plots specific to that set of experiments
  * `results*.csv`: column names can be found in the analysis files
* `Makefile`: some builds are used manually, and some are used by scripts such as `experiment.py`
* `plot_experiment.py`: after performing an experiment with `experiment.py`, this is an easy way to visualise the results
* `plots`: all plots that are not from `local_experiments`
* `plotting_code`: R and Python scripts for plotting
  * `plot_cpu_experiments.R`: plots CPU data from `data`
  * `plot_function.py`: used to illustrate how function-based workload works
  * `plot_memory_experiments.R`: plots memory usage data from `data`
  * `plottting.R`: a helper function that draws better heatmaps (with the colour bar scaled and adjusted)
* `prometheus`: the Prometheus MiniShift addon from [here](https://github.com/minishift/minishift-addons/tree/master/add-ons/prometheus) with minor modifications
* `report`: you might want to read it
  * `talk`: slides for a talk midway through the project
* `src/main/java`: all Java classes meant for the actual application (rather than separate experiments)
* `validator.R`: version 0.1 of a validator that can determine whether memory usage is as expected
