<!---
# This file is part of the pl.wrzasq.commons.
#
# @license http://mit-license.org/ The MIT license
# @copyright 2018 - 2019 © by Rafał Wrzeszcz - Wrzasq.pl.
-->

# Custom resources

[**CloudFormation**](https://aws.amazon.com/cloudformation/) allows defining [custom resources](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-custom-resources.html),
which allow binding own logic to template execution. It can be implementated as a [**Lambda**](https://aws.amazon.com/lambda/)
function and then triggered when stack operation is executed.

## Handler

To handle such actions, function need to be designed to work with **CloudFormation** request and response structure.
`pl.wrzasq.commons.aws.cloudformation.CustomResourceHandler` class covers interacting with **CloudFormation**
requests, leaving just actions implementation for you.

There are three actions you need to provide for the handler, that corresponds to three **CloudFormation** request
types:
-   `Create`, when resource creation is triggered;
-   `Update`, when resource properties got changed;
-   `Delete`, when resource deletion is requested.

**Note:** You just need to provide callbacks, you can provide same callback for multiple actions.

```java
class S3Upload
{
    @Data
    public static class ResourceProperties
    {
        private String source;

        private String destination;
    }

    @Data
    public static class ResourceOutputs
    {
        private long uploadedFilesCount;
    }

    public CustomResourceResponse<S3Upload.ResourceOutputs> upload(S3Upload.ResourceProperties request)
    {
        // handle files upload and build output properties
    }

    public CustomResourceResponse<S3Upload.ResourceOutputs> cleanup(S3Upload.ResourceProperties request)
    {
        // handle files cleanup and build output
        // output needs to have structure, but it's properties are meaningless in this case
    }
}

public class MyLambda
{
    private static CustomResourceHandler<CustomResourceHandler.ResourceProperties, CustomResourceHandler.ResourceOutputs> handler;

    static {
        S3Upload resource = new S3Upload();

        // (1)
        MyLambda.handler = new CustomResourceHandler<>(
            resource::upload,
            resource::upload,
            resource::cleanup
        );
    }

    public static void entryPoint(CfnRequest<CustomResourceHandler.ResourceProperties> request, Context context)
    {
        // (2)
        MyLambda.handler.handle(request, context);
    }
}
```

1.  All you expose to the handler are callbacks for your resource actions. You will receive input resource properties
passed from template as a parameter.
1.  Your **Lambda** entry point gets lean - just pass the execution to the generic handler. Both arguments are
important, as they are used to compute **CloudFormation** response. Without any of the information it will not be
possible to send outcome back and your stack will get stuck!

## `CustomResourceResponse`

Ok, what is `CustomResourceResponse`? You are mainly interested in returning the data. But apart from that
**CloudFormation** also takes some additional meta-information. The one you can actually interact with is
`physicalResourceId` which identifies resource created by your handler.

`pl.wrzasq.commons.aws.cloudformation.CustomResourceResponse` just wraps your data together with resource information
(for the moment just physical ID).

You can return response in two ways - if you are not interested in physical ID, just wrap your data (it will set ID to
`null` indicating default one):

```java
return new CustomResourceResponse<>(yourData);
```

Or if you want to set it explicitely:

```java
return new CustomResourceResponse<>(yourData, yourResourceId);
```

Why `physicalResourceId` is important? Because it identifies resource actually created by your handler. This way you
can inform **CloudFormation** if an update to your resource caused new resource creation (return new resource ID on
update) or you updated existing one (return same physical resource ID on update that you computed/acquired in creation).
If you use different physical resource ID **CloudFormation** will try to delete old resource in the cleanup phase.
