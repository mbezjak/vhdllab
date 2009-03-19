package hr.fer.zemris.vhdllab.service.util;

import org.apache.commons.lang.SystemUtils;

public abstract class OsArchitectureUtils {

    public static final String ARCH_I386 = "i386";
    public static final String ARCH_I486 = "i486";
    public static final String ARCH_I586 = "i586";
    public static final String ARCH_I686 = "i686";
    public static final String ARCH_X86 = "x86";

    public static final String ARCH_X86_64 = "x86_64";
    public static final String ARCH_X86_64_2 = "x86-64";
    public static final String ARCH_X64 = "x64";
    public static final String ARCH_AMD64 = "amd64";
    public static final String ARCH_IA64 = "IA64";
    public static final String ARCH_IA64_2 = "IA-64";

    public static final boolean IS_32_BIT = is(ARCH_I386) ||
                                            is(ARCH_I486) ||
                                            is(ARCH_I586) ||
                                            is(ARCH_I686) ||
                                            is(ARCH_X86);

    public static final boolean IS_64_BIT = is(ARCH_X86_64) ||
                                            is(ARCH_X86_64_2) ||
                                            is(ARCH_X64) ||
                                            is(ARCH_AMD64) ||
                                            is(ARCH_IA64) ||
                                            is(ARCH_IA64_2);

    private static boolean is(String arch) {
        return arch.equalsIgnoreCase(SystemUtils.OS_ARCH);
    }

}
