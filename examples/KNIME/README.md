#User Agent Example

This KNIME workflow shows a simple example using the User Agent Parser component in KNIME using some log file data.  The workflow splits into two different paths.  The upper path demonstrates the use of the User Agent Parser KNIME node.  

The lower path shows how to use the extension functions can be used from the Drive Fields node.   This method of parsing the user agent string is less efficient and should only be used in cases where performance is not important or where a small subset of the user agent parts are needed.

Once you import and load the [KNIME workflow](https://github.com/ActianCorp/df-useragent/blob/master/examples/KNIME/UserAgent.zip)
you will see the following workflow on the canvas.   The workflow is configured and ready to run.


![Example workflow](https://raw.githubusercontent.com/ActianCorp/df-useragent/master/examples/KNIME/UserAgent.png)
