package com.blackbox.plog.pLogs.filter

import android.util.Log
import com.blackbox.plog.pLogs.PLog
import com.blackbox.plog.utils.DateControl
import com.blackbox.plog.utils.DateTimeUtils
import java.io.File

/**
 * Created by umair on 03/01/2018.
 */
internal object FileFilter {

    internal val TAG = FileFilter::class.java.simpleName

    /*
     * Filter files by 'Today'.
     */
    fun getFilesForToday(folderPath: String): List<File> {
        val directory = File(folderPath)
        val list = directory.listFiles()
        return list.asList()
    }


    /*
     * Filter files by '24Hours'.
     */
    fun getFilesForLast24Hours(folderPath: String): List<File> {

        val today = Integer.parseInt(DateControl.instance.currentDate)
        val lastDay = Integer.parseInt(DateControl.instance.lastDay)

        val directory = File(folderPath)
        val files = directory.listFiles()

        val lisOfFiles = arrayListOf<File>()

        if (files != null) {
            for (file in files) {
                if (file != null) {
                    if (file.isDirectory) {
                        val day = FilterUtils.extractDay(file.name)

                        if (PLog.pLogger.isDebuggable)
                            Log.i(FileFilter.TAG, "Files between dates: $lastDay & $today,Date File Present: $day")

                        if (lastDay < today) {
                            if (day <= today && day >= lastDay) {
                                lisOfFiles.addAll(getFilesForToday(file.path))
                            }
                        } else {
                            if (day <= today) {
                                lisOfFiles.addAll(getFilesForToday(file.path))
                            }
                        }
                    }
                }
            }
        }

        return lisOfFiles
    }

    /*
     * Filter files by 'Week'.
     */
    fun getFilesForLastWeek(folderPath: String): List<File> {

        val lisOfFiles = arrayListOf<File>()
        val listOfDates = DateTimeUtils.getDatesBetween()

        if (PLog.pLogger.isDebuggable)
            Log.i(FileFilter.TAG, "Files between dates: ${listOfDates.first()} & ${listOfDates.last()}")

        for (date in listOfDates) {

            val dateDirectory = File(folderPath + File.separator + date)

            if (dateDirectory.isDirectory) {
                val files = dateDirectory.listFiles()

                for (file in files) {
                    lisOfFiles.addAll(getFilesForToday(file.path))
                }
            }
        }

        return lisOfFiles
    }

    /*
     * Filter files by 'Hour'.
     */
    fun getFilesForLastHour(folderPath: String): List<File> {

        val lisOfFiles = arrayListOf<File>()
        val directory = File(folderPath)
        val files = directory.listFiles()

        val lastHour = Integer.parseInt(DateControl.instance.hour) - 1

        if (files.isNotEmpty()) {

            for (i in files.indices) {
                val fileHour = FilterUtils.extractHour(files[i].name)

                if (PLog.pLogger.isDebuggable)
                    Log.i(FileFilter.TAG, "Last Hour: " + lastHour + " Check File Hour: " + fileHour + " " + files[i].name)

                if (fileHour == lastHour) {
                    lisOfFiles.add(files[i])
                }
            }

        }

        return lisOfFiles
    }

}
