import modules.ModuleNames.Domain
import modules.ModuleNames.Features
import modules.ModuleNames.Logger
import modules.ModuleNames.MainApp

include(
    MainApp,
    Logger,
    Domain,
    Features.SharedAssets
)