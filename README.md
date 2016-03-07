Cache Loop Simulator

This application pretends to simulate at a basic level the loop test performed on a computer to characterize it's memory hierarchy. It was a two-week project made from scratch and it should not be taken as an OOP example. This tool does not provide an accurate simulation of real-world memory hierarchy as it hugely simplifies memory accesses (it completely neglects TLB, virtual memory, and time variations due to write policies), however it is accurate enough to visualize the patterns hidden behind the locality influence on cpu's performance. With that in mind, it is meant to give computer architecture students a little more insight into the behavior of memory hierarchy.

For those interested in this subject I would recommend to have a look at the following bibliography:

"Computer Architecture: A Quantitative Approach" (5th edition) by Hennessy and Patterson, Appendix B and Chapter 2 of this book both explain at basic to intermediate level how caches work, as well as virtual addressing and memory hierarchy design techniques which are used in modern computers.

"CPU performance evaluation and execution time prediction using narrow spectrum benchmarking" (1992), by Saavedra-Barrera, has it Chapter 5 dedicated to memory hierarchy characterization through loop test, and there it explains all the details of this technique.


About the implementation:

The program runs and simulates explicitly every single access to memory, and has proper structures to represent each type of cache. It does not use formulas to predict any of the results shown in the graphs. As for this first release, the application is only able to run loop tests on customized memory hierarchy. Originally it was meant to be a cache simulation with more scope, allowing the user to simulate arbitrary memory references sequences and to view step-by-step how the contents of the cache were updated. Even though the code is prepared to easily add this functionality, I preferred not to do so in order to have a clean first version to release, and because it would take more time and effort than expected in building the GUI. Maybe in a future update these features will be added in. 
With regard to the "loop guess" feature, it is worth mentioning that the caches presented to the user to guess are randomly generated, and they do not represent real world memory hierarchies. Even though the generator method has been thought to produce plausible scenarios, rather than just throw numbers in, just bear in mind that you might find some strange-looking caches or even ambiguous plots due to the randomness of the generation.
The interface of the programm was made using Swing, and for the plots I used an external library called JFreeChart (version 1.0.19), which uses JCommon 1.0.23 library. In order to compile the code you will need both, which can be downloaded from: http://www.jfree.org/jfreechart/download.html.


Contact mail: juanmanuelfresia@gmail.com
Any bug reports, comments or suggestions are always welcome. 

Made by Juan Manuel Fresia, engineer student at University of Buenos Aires.

