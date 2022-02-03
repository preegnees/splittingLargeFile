import java.io.*
import java.nio.file.Files


fun main(){
    val t1 = System.currentTimeMillis()
//    create(file)
//    merge(getAllFiles(parent), newFile)
    val t2 = System.currentTimeMillis()
    println((t2 - t1)/1000.0)
}

fun create(f: File) {
    var partCounter = 1
    val sizeOfFiles = 1024 * 1024
    val buffer = ByteArray(sizeOfFiles)
    val fileName = f.name
    FileInputStream(f).use { fis ->
        BufferedInputStream(fis).use { bis ->
            var bytesAmount = 0
            while (bis.read(buffer).also { bytesAmount = it } > 0) {
                val filePartName = "$f---${partCounter++}"
                val newFile = File(filePartName)
                FileOutputStream(newFile).use { out -> out.write(buffer, 0, bytesAmount) }
            }
        }
    }
}

fun merge(files: List<File>, out: File) {
    FileOutputStream(out).use { fos ->
        BufferedOutputStream(fos).use { mergingStream ->
            for (f in files) {
                Files.copy(f.toPath(), mergingStream)
            }
        }
    }
}

fun getAllFiles(path: File): MutableList<File> {
    val needFiles = mutableListOf<File>()
    val listFiles = path.listFiles()
    if (!listFiles.isNullOrEmpty()) {
        for (i in listFiles) {
            if (i.name.split("---").size > 1) {
                needFiles.add(i)
            }
        }
    }
    return needFiles
}