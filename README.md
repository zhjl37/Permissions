# Permissions

Simplifying Android Permissions

![](https://raw.githubusercontent.com/zhjl37/Permissions/main/screenshot-1.png)
![](https://raw.githubusercontent.com/zhjl37/Permissions/main/screenshot-2.png)
![](https://raw.githubusercontent.com/zhjl37/Permissions/main/screenshot-3.png)

## Gradle

```
implementation '...'
```

## Usage

This usage shows how to request multiple permissions.

### Declare required permissions

```
companion object {
    private val PERMISSIONS_REQUIRED = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}

private val permissions = multiplePermissions(PERMISSIONS_REQUIRED, ::onActivityResult)        
```

### Request permissions

```
if (permissions.request()) {
    TODO("required permissions are granted.")
}
```

### Handle permission result

```
private fun onActivityResult(result: Map<String, Boolean>) {
    TODO("check permissions result, and do something")
}
```

### Custom your PermissionsRationaleActivity
```
...
```
