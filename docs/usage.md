# How to use Cache Loop Simulator

This application has two modes of operation: the _loop simulaiton_ mode and the
_loop guess_ mode. The first one allows you to design a bank of cache memories
up to three levels and then run a loop simulation to see when and how it starts
trashing. The _loop guess_ mode works the other way around, it gives you the
results of a loop test simulation over an **unknown** cache memory and asks you
to try and figure out its parameters.

## Loop simulation

This mode is selected by default in the main screen as soon as the app is
launched.

!["Loop run" screen][img/loop_run.png]

There is a separate set of configurations for each level of the cache.
Use the sliders to choose the block size, total size, hit time and associativity
for each level. The main memory access time can be configured in a separate
slider. 

On the bottom right you can enable and disable the L2 and L3 cache levels, as
well as the max size of the test loop. The _Use speculation_ option makes
the test run faster by calculating the outcome, instead of simulating every
access (I recommend to leave it set).

Finally, hitting the "Run Loop" button will trigger the simulation and prompt
you with the results.

![Test results][img/loop_test_result.png]

## Loop guess

By choosing the second tab on the top you can enter the "Loop guess" mode. The
controls will change slightly.

!["Loop guess" screen][img/loop_guess.png]

Here you can test your understanding of the loop performance test of a cache by
deducting a cache bank parameters from the test results. Start by choosing int
the bottom left the maximum level of the cache to simulate, then hit "New plot"
button.

You will be prompted with a graphics similar to the one shown in the previous
section, but with the difference that the parameters used to generate it are
unknown to you.

Once you have deducted the parameters from the graphic, fill them by using the
sliders and hit "Make guess". The box in the bottom right will inform you if
your guess was correct or not.

!["Correct guess"][img/correct_answer.png]

If you want to know the answer right away, use the "Show answer" button. You can
always hit the "Show plot" button to reopen the current test result.

!["Show answer"][img/show_answer.png]


