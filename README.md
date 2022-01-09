# Permissions

Simplifying Android Permissions

![](https://raw.githubusercontent.com/zhjl37/Permissions/main/screenshot-1.png)
![](https://raw.githubusercontent.com/zhjl37/Permissions/main/screenshot-2.png)
![](https://raw.githubusercontent.com/zhjl37/Permissions/main/screenshot-3.png)

- The 1st screenshot illustrates the UI
- The 2nd screenshot illustrates the UI after the user denied the permissions, and selected with 'Never asked again' option.
- The 3nd screenshot illustrates the rationale dialog for requesting permissions after the user denied the permissions, and selected with 'Never asked again' option.

> **Note**: 
> The permissions rationale dialog is shown only after the user denied the permissions, and selected with 'Never asked again' option.

![](https://raw.githubusercontent.com/zhjl37/Permissions/main/screenrecord.gif)

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

private val permissions = multiplePermissions(
    permissions = PERMISSIONS_REQUIRED,
    callback = ::onActivityResult
)

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
