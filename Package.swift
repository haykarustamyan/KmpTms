// swift-tools-version:5.3
import PackageDescription

let package = Package(
   name: "Shared",
   platforms: [
     .iOS(.v14),
   ],
   products: [
      .library(name: "Shared", targets: ["Shared"])
   ],
   targets: [
      .binaryTarget(
         name: "Shared",
         url: "https://github.com/haykarustamyan/KmpTms/releases/download/shared_1.0.0/Shared.xcframework.zip",
         checksum:"5ecbca8543b6456dca8a5251827f83720cbbd48519146a9c9c67ebea94114e62")
   ]
)