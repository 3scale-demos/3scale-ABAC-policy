# APIcast GUID Policy

## Overview

[Official Documentation](https://access.redhat.com/documentation/en-us/red_hat_3scale_api_management/2.8/html/administering_the_api_gateway/apicast_policies#creating_custom_apicast_policies)

This policy can be used to add a GUID into the header for APIcast and trace the request from the gateway to the backend.

There are two builds that we need to keep in mind.

- `apicast-new-policies`: This first runs the default source to image scripts and copies the contents of the [policies](policies) folder.
- `apicast-custom`: Takes the output of the first build, runs copies the contents to the right place, also runs some tests.

So, both of them are required to run in the same order that it is going to be shown later.

---

**NOTE**

If you are taking this as a reference to develop your own policy then don't forget the following rules:

1. Update the policy folder name inside the [policies](policies) folder with the name of your policy.
2. Update the policy folder with the version of your policy.
3. Update the [apicast-policy.json](policies/guid/0.1/apicast-policy.json) with the information of your policy. Here you can add parameters to it.
4. The other lua files inside [policies/guid/0.1](policies/guid/0.1) are where you will need to implement your policy.
5. Update the [openshift.yml:L70](https://github.com/mgohashi/3scale-custom-policy/blob/382253ce1c44d6876f76c3cd288764e4498999a7/openshift.yml#L70) with dependencies

---

Parameters of this policy:
- Header name: If not provided, it will use the deafult header name which is `GUID`.

## Installation on OpenShift

1. Update the following lines in openshift.yml with your own envrironment.
	
    - GIT_REPO openshift.yml:L113
    - APICAST_CUSTOM_NAMESPACE openshift.yml:L117

2. Access the current 3scale namespace for your API casts.

   ```shell
   oc project <<3SCALE_NAMESPACE>>
   ```

3. Create the image stream of the apicast.

   ```shell
   oc -n <<3SCALE_NAMESPACE>> import-image amp-apicast-custom:3scale2.8.0 --from=registry.redhat.io/3scale-amp2/apicast-gateway-rhel8:3scale2.8.0 --confirm
   ```

4. To install the build configs on OpenShift you can use provided template:

   ```shell
   oc -n <<3SCALE_NAMESPACE>> new-app -f openshift.yml -o yaml | oc apply -f -
   ```

## Starting the build

1. To start the first build run the following command:

   ```shell
   oc -n <<3SCALE_NAMESPACE>> start-build apicast-new-policy --wait --follow
   ```

2. To start the second build run the following command:

   ```shell
   oc -n <<3SCALE_NAMESPACE>> start-build apicast-custom --wait --follow
   ```

If you didn't change the output image of the second build, you should see the API Casts (stage and production) being redeployed.

Once the redeploys finish the new policy appearing in the list of policies to add.

## Configuring API

1. Log into your Admin portal;
2. From the dropdown menu on the top Access your API or Service and click on `Integration` > `Policies`;
3. Then click on the link `Add policy`;
4. Then click on the `GUID Policy`:
5. Move the new policy to before the default **API Cast** policy;
6. Click on the **GUID Policy** again and you should see its properties;
7. There you can change the header name to be used to add the **UUID** value;
8. Once you finish changing the settings, you can click on **Update policy** button and then `Update Policy Chain`;
9. Go to configuration and promote your changes to staging;
10. Finally you can test your new policy.

## Testing your changes

A sample response of the configured API:

---
**NOTE**

Some attributes have been omitted for brevity.

---

```json
{
    "method": "GET",
    "path": "/",
    "args": "",
    "body": "",
    "headers": {
        ...,
        "HTTP_GUID": "69581a25-685f-4bbc-8224-2d8aea9b38de",
        ...
    },
    "uuid": "89f9dca6-c85d-4044-a3ff-4a3d4e32c4e2"
}
```

