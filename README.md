# New-View-on-Adversarial-Queuing-on-MAC
The code of the simulation for the paper "New View on Adversarial Queuing on MAC" by Elijah Hradovich, Marek Klonowski and Dariusz R. Kowalski, appeared in IEEE Communication Letters 2021

The purpose of this code is to conduct simulations in the dry Multiple Access Channel model in adversarial environment.
The simulation flow is as follows:
* The Main class reads run arguments, choses the algorithm to run on Stations, sets the execution length in rounds and the size of the system (number of stations); Simulation instance gets created
* The Simulation class has a hardcoded constant of repetitions, used to define how many experiments should be run in order to get the average stats of them
* The Simulation class for each execution creates a new Channel with Stations attached to it, as well as an adversary strategy described in the paper for the chosen algorithm
* The Simulation is executed in ticks, with the order following the MAC model; stats are collected for each tick
* In the end of the execution stats are printed
